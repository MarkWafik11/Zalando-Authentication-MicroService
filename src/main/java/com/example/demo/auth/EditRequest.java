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
public class EditRequest {

    private String email;
    private String password;

    private String firstName;
    private String lastName;
    private String address;
    private String telephoneNumber;
    private LocalDate dateOfBirth;

    private String brandName;
    private String hotline;
    private LocalDate dateJoined;
}