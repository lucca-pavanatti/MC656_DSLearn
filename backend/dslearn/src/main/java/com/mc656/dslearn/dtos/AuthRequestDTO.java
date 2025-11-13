package com.mc656.dslearn.dtos;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String idToken; // ID token from Google Sign-In
}
