package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/customer/register")
    public ResponseEntity<AuthenticationResponse> customerRegister(
            @RequestBody CustomerRegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.customerRegister(request));
    }
    @PostMapping("/merchant/register")
    public ResponseEntity<AuthenticationResponse> merchantRegister(
            @RequestBody MerchantRegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.merchantRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PutMapping("/editProfile")
    public void editProfile(
            @RequestBody EditRequest editRequest,
            HttpServletRequest request
    ) {
        authenticationService.editProfile(editRequest, request);
    }
}