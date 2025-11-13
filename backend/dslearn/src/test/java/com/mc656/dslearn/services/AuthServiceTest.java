package com.mc656.dslearn.services;

import com.mc656.dslearn.config.GoogleConfig;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoogleConfig googleConfig;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void verifyAndCreateUser_WithInvalidToken_ShouldThrowGeneralSecurityException() {
        String idToken = "invalid-token";

        when(googleConfig.getClientId()).thenReturn("test-client-id");

        assertThrows(Exception.class, () -> {
            authService.verifyAndCreateUser(idToken);
        });
    }

    @Test
    void verifyTokenAndGetUserEntity_WithInvalidToken_ShouldThrowGeneralSecurityException() {
        String idToken = "invalid-token";

        when(googleConfig.getClientId()).thenReturn("test-client-id");

        assertThrows(Exception.class, () -> {
            authService.verifyTokenAndGetUserEntity(idToken);
        });
    }
}
