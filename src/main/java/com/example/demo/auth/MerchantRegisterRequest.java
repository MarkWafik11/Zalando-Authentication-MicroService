package com.example.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRegisterRequest {

    private String email;
    private String password;

    private String brandName;
    private String hotline;
    private LocalDate dateJoined;
}
