package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.merchant.Merchant;
import com.example.demo.merchant.MerchantRepository;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.token.TokenType;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> userWithThisEmail = userRepository.findUserByEmail(request.getEmail());
        if(userWithThisEmail.isPresent()){
            throw new IllegalStateException("Email already taken");
        }

        var user = User.builder()
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(request.getRole().equals("customer")){
            Customer customer = new Customer();
            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setAddress(request.getAddress());
            customer.setDateOfBirth(request.getDateOfBirth());
            customer.setTelephoneNumber(request.getTelephoneNumber());
            customer.setUserDetails1(user);
            customerRepository.save(customer);
        }
        else{
            Merchant merchant = new Merchant();
            merchant.setBrandName(request.getBrandName());
            merchant.setDateJoined(request.getDateJoined());
            merchant.setHotline(request.getHotline());
            merchant.setUserDetails2(user);
            merchantRepository.save(merchant);
        }

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    @Transactional
    public void editProfile(EditRequest editRequest, HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if(storedToken.isExpired() || storedToken.isRevoked()){
            throw new IllegalStateException("Not logged in");
        }
        if (storedToken != null) {
            var user = storedToken.getUser();
            if(editRequest.getEmail() != null && editRequest.getEmail().length() > 0 &&
            !editRequest.getEmail().equals(user.getEmail())){
                Optional<User> userWithThisEmail = userRepository.findUserByEmail(editRequest.getEmail());
                if(userWithThisEmail.isPresent()){
                    throw new IllegalStateException("Email already taken");
                }
                user.setEmail(editRequest.getEmail());
            }
            if(editRequest.getPassword() != null && editRequest.getPassword().length() > 0){
                user.setPassword(passwordEncoder.encode(editRequest.getPassword()));
            }
            userRepository.save(user);

            if(user.getRole().equals("customer")){
                var customerToBeEdited = customerRepository.findCustomerById(user.getId());
                if(editRequest.getFirstName() != null && editRequest.getFirstName().length() > 0){
                    customerToBeEdited.get().setFirstName(editRequest.getFirstName());
                }
                if(editRequest.getLastName() != null && editRequest.getLastName().length() > 0){
                    customerToBeEdited.get().setLastName(editRequest.getLastName());
                }
                if(editRequest.getAddress() != null && editRequest.getAddress().length() > 0){
                    customerToBeEdited.get().setAddress(editRequest.getAddress());
                }
                if(editRequest.getTelephoneNumber() != null && editRequest.getTelephoneNumber().length() > 0){
                    customerToBeEdited.get().setTelephoneNumber(editRequest.getTelephoneNumber());
                }
                if(editRequest.getDateOfBirth() != null){
                    customerToBeEdited.get().setDateOfBirth(editRequest.getDateOfBirth());
                }
                customerRepository.save(customerToBeEdited.get());
            }

            else if(user.getRole().equals("merchant")){
                var merchantToBeEdited = merchantRepository.findMerchantById(user.getId());
                if(editRequest.getBrandName() != null && editRequest.getBrandName().length() > 0){
                    merchantToBeEdited.get().setBrandName(editRequest.getBrandName());
                }
                if(editRequest.getHotline() != null && editRequest.getHotline().length() > 0){
                    merchantToBeEdited.get().setHotline(editRequest.getHotline());
                }
                if(editRequest.getDateJoined() != null){
                    merchantToBeEdited.get().setDateJoined(editRequest.getDateJoined());
                }
                merchantRepository.save(merchantToBeEdited.get());
            }
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
