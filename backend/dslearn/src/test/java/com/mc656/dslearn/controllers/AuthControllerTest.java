package com.mc656.dslearn.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc656.dslearn.dtos.AuthRequestDTO;
import com.mc656.dslearn.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void auth_WithValidToken_ShouldReturnCreated() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setIdToken("valid-google-token");

        doNothing().when(authService).verifyAndCreateUser("valid-google-token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).verifyAndCreateUser("valid-google-token");
    }

    @Test
    void auth_WithInvalidToken_ShouldReturnBadRequest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setIdToken("invalid-token");

        doThrow(new GeneralSecurityException("Invalid ID token"))
                .when(authService).verifyAndCreateUser("invalid-token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Token verification failed")));

        verify(authService, times(1)).verifyAndCreateUser("invalid-token");
    }

    @Test
    void auth_WhenIOExceptionOccurs_ShouldReturnBadRequest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setIdToken("some-token");

        doThrow(new IOException("Network error"))
                .when(authService).verifyAndCreateUser("some-token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Token verification failed")));

        verify(authService, times(1)).verifyAndCreateUser("some-token");
    }

    @Test
    void auth_WithExistingUser_ShouldStillReturnCreated() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setIdToken("existing-user-token");

        doNothing().when(authService).verifyAndCreateUser("existing-user-token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).verifyAndCreateUser("existing-user-token");
    }

    @Test
    void auth_WithEmptyToken_ShouldCallService() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setIdToken("");

        doThrow(new GeneralSecurityException("Invalid ID token"))
                .when(authService).verifyAndCreateUser("");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).verifyAndCreateUser("");
    }
}
