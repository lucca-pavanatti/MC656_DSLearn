package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.AuthRequestDTO;
import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Authenticate user", description = "Verify Google ID token and create/update user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "400", description = "Token verification failed")
    })
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDTO req) {
        try {
            authService.verifyAndCreateUser(req.getIdToken());
            return ResponseEntity.status(201).build();
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(400).body("Token verification failed: " + e.getMessage());
        }
    }

}
