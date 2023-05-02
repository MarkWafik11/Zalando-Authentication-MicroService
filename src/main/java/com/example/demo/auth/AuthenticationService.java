package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.merchant.Merchant;
import com.example.demo.merchant.MerchantRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if(request.getRole().equals("customer")){
            Customer customer = new Customer();
            customer.setName(request.getName());
            customer.setAddress(request.getAddress());
            customer.setDateOfBirth(request.getDateOfBirth());
            customer.setTelephoneNumber(request.getTelephoneNumber());
            customer.setUserDetails1(user);
            customerRepository.save(customer);
        }
        else{
            System.out.print("HEHEHHE");
            Merchant merchant = new Merchant();
            merchant.setBrandName(request.getBrandName());
            merchant.setDateJoined(request.getDateJoined());
            merchant.setHotline(request.getHotline());
            merchant.setUserDetails2(user);
            merchantRepository.save(merchant);
        }

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
       // saveUserToken(savedUser, jwtToken);
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
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        //revokeAllUserTokens(user);
        //saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)

                .build();
    }


}
