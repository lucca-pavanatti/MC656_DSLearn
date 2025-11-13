package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.UserInfoDTO;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
    void getUserInfoByToken_WithValidToken_ShouldReturnUserInfo() throws GeneralSecurityException, IOException {
        String token = "valid-token";

        when(authService.verifyTokenAndGetUserEntity(token)).thenReturn(testUser);

        UserInfoDTO result = userService.getUserInfoByToken(token);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());

        verify(authService, times(1)).verifyTokenAndGetUserEntity(token);
    }

    @Test
    void getUserInfoByToken_WithInvalidToken_ShouldReturnNull() throws GeneralSecurityException, IOException {
        String token = "invalid-token";

        when(authService.verifyTokenAndGetUserEntity(token)).thenReturn(null);

        UserInfoDTO result = userService.getUserInfoByToken(token);

        assertNull(result);
        verify(authService, times(1)).verifyTokenAndGetUserEntity(token);
    }
    @Test
    void updateUserInfoFromToken_WithValidToken_ShouldUpdateAndReturnUserInfo() throws GeneralSecurityException, IOException {
        String token = "valid-token";

        when(authService.verifyTokenAndGetUserEntity(token)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserInfoDTO result = userService.updateUserInfoFromToken(
                token
        );

        assertNotNull(result);

        verify(authService, times(1)).verifyTokenAndGetUserEntity(token);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUserInfoFromToken_WithNullToken_ShouldReturnNull() throws GeneralSecurityException, IOException {
        String token = "invalid-token";

        when(authService.verifyTokenAndGetUserEntity(token)).thenReturn(null);

        UserInfoDTO result = userService.updateUserInfoFromToken(
                token
        );

        assertNull(result);
        verify(authService, times(1)).verifyTokenAndGetUserEntity(token);
        verify(userRepository, never()).save(any(User.class));
    }
}
