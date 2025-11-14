package com.mc656.dslearn.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc656.dslearn.dtos.ExerciseProgressDTO;
import com.mc656.dslearn.dtos.ExerciseStatusRequestDTO;
import com.mc656.dslearn.dtos.TopicProgressDTO;
import com.mc656.dslearn.dtos.TopicStatusRequestDTO;
import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.services.UserExerciseService;
import com.mc656.dslearn.services.UserService;
import com.mc656.dslearn.services.UserTopicService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserExerciseService userExerciseService;

    @Mock
    private UserTopicService userTopicService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getUserInfo_WithValidToken_ShouldReturnUserInfo() throws Exception {
        String token = "valid-token";
        UserInfoDTO userInfo = UserInfoDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        when(userService.getUserInfoByToken(token)).thenReturn(userInfo);

        mockMvc.perform(get("/user/info")
                        .header("Token", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserInfoByToken(token);
    }

    @Test
    void getUserInfo_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        String token = "invalid-token";

        when(userService.getUserInfoByToken(token)).thenReturn(null);

        mockMvc.perform(get("/user/info")
                        .header("Token", token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));

        verify(userService, times(1)).getUserInfoByToken(token);
    }

    @Test
    void getUserInfo_WhenTokenVerificationFails_ShouldReturnBadRequest() throws Exception {
        String token = "malformed-token";

        when(userService.getUserInfoByToken(token))
                .thenThrow(new GeneralSecurityException("Token verification failed"));

        mockMvc.perform(get("/user/info")
                        .header("Token", token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Token verification failed")));

        verify(userService, times(1)).getUserInfoByToken(token);
    }

    @Test
    void getUserInfo_WhenIOExceptionOccurs_ShouldReturnBadRequest() throws Exception {
        String token = "valid-token";

        when(userService.getUserInfoByToken(token))
                .thenThrow(new IOException("Network error"));

        mockMvc.perform(get("/user/info")
                        .header("Token", token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Token verification failed")));

        verify(userService, times(1)).getUserInfoByToken(token);
    }

    @Test
    void updateExerciseStatus_WithValidData_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        ExerciseStatusRequestDTO request = new ExerciseStatusRequestDTO();
        request.setExerciseId(1L);
        request.setStatus("completed");

        doNothing().when(userExerciseService).updateStatus(userId, 1L, "completed");

        mockMvc.perform(put("/user/{userId}/exercises/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userExerciseService, times(1)).updateStatus(userId, 1L, "completed");
    }

    @Test
    void updateTopicStatus_WithValidData_ShouldReturnOk() throws Exception {
        Long userId = 1L;
        TopicStatusRequestDTO request = new TopicStatusRequestDTO();
        request.setTopicName("arrays");
        request.setStatus("started");

        doNothing().when(userTopicService).updateStatus(userId, "arrays", "started");

        mockMvc.perform(put("/user/{userId}/topics/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userTopicService, times(1)).updateStatus(userId, "arrays", "started");
    }

    @Test
    void getExercisesProgress_WithValidUserId_ShouldReturnProgress() throws Exception {
        Long userId = 1L;
        List<ExerciseProgressDTO> progress = Arrays.asList(
                ExerciseProgressDTO.builder()
                        .id(1L)
                        .title("Exercise 1")
                        .status("completed")
                        .build(),
                ExerciseProgressDTO.builder()
                        .id(2L)
                        .title("Exercise 2")
                        .status("started")
                        .build()
        );

        when(userExerciseService.getExercisesProgress(userId)).thenReturn(progress);

        mockMvc.perform(get("/user/{userId}/exercises/progress", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Exercise 1"))
                .andExpect(jsonPath("$[0].status").value("completed"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Exercise 2"))
                .andExpect(jsonPath("$[1].status").value("started"));

        verify(userExerciseService, times(1)).getExercisesProgress(userId);
    }

    @Test
    void getTopicsProgress_WithValidUserId_ShouldReturnProgress() throws Exception {
        Long userId = 1L;
        List<TopicProgressDTO> progress = Arrays.asList(
                TopicProgressDTO.builder()
                        .name("arrays")
                        .status("completed")
                        .build(),
                TopicProgressDTO.builder()
                        .name("linked-lists")
                        .status("started")
                        .build()
        );

        when(userTopicService.getTopicsProgress(userId)).thenReturn(progress);

        mockMvc.perform(get("/user/{userId}/topics/progress", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("arrays"))
                .andExpect(jsonPath("$[0].status").value("completed"))
                .andExpect(jsonPath("$[1].name").value("linked-lists"))
                .andExpect(jsonPath("$[1].status").value("started"));

        verify(userTopicService, times(1)).getTopicsProgress(userId);
    }

    @Test
    void getExercisesProgress_WithNoProgress_ShouldReturnEmptyList() throws Exception {
        Long userId = 1L;

        when(userExerciseService.getExercisesProgress(userId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/user/{userId}/exercises/progress", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(userExerciseService, times(1)).getExercisesProgress(userId);
    }

    @Test
    void getTopicsProgress_WithNoProgress_ShouldReturnEmptyList() throws Exception {
        Long userId = 1L;

        when(userTopicService.getTopicsProgress(userId)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/user/{userId}/topics/progress", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(userTopicService, times(1)).getTopicsProgress(userId);
    }
}
