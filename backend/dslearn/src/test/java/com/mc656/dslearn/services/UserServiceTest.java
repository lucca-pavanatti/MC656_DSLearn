package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.dtos.UserMetricsDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserExercise;
import com.mc656.dslearn.models.UserTopic;
import com.mc656.dslearn.repositories.DSARepository;
import com.mc656.dslearn.repositories.ExerciseRepository;
import com.mc656.dslearn.repositories.UserExerciseRepository;
import com.mc656.dslearn.repositories.UserRepository;
import com.mc656.dslearn.repositories.UserTopicRepository;
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

    @Mock
    private UserExerciseRepository userExerciseRepository;

    @Mock
    private UserTopicRepository userTopicRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private DSARepository dsaRepository;

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

    @Test
    void getUserMetrics_WithSampleData_ShouldReturnExpectedMetrics() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(testUser));

        DSATopic topic = DSATopic.builder().id(10L).name("arrays").build();
        when(dsaRepository.findAllTopics()).thenReturn(Arrays.asList(topic));

        Exercise exercise = Exercise.builder()
                .id(100L)
                .title("Ex 1")
                .difficulty(com.mc656.dslearn.models.enums.Difficulty.Easy)
                .relatedTopics("arrays")
                .build();
        when(exerciseRepository.findAllExercises()).thenReturn(Arrays.asList(exercise));

        UserTopic userTopic = UserTopic.builder().user(testUser).topic(topic).status("COMPLETED").build();
        when(userTopicRepository.findByUserId(userId)).thenReturn(Arrays.asList(userTopic));

        UserExercise userExercise = UserExercise.builder().user(testUser).exercise(exercise).status("COMPLETED").build();
        when(userExerciseRepository.findByUserId(userId)).thenReturn(Arrays.asList(userExercise));

        UserMetricsDTO metrics = userService.getUserMetrics(userId);

        assertNotNull(metrics);
        assertEquals(1, metrics.getTotalTopics());
        assertEquals(1, metrics.getCompletedTopics());
        assertEquals(0, metrics.getInProgressTopics());
        assertEquals(0, metrics.getNotStartedTopics());

        assertEquals(1, metrics.getTotalExercises());
        assertEquals(1, metrics.getCompletedExercises());
        assertEquals(0, metrics.getInProgressExercises());
        assertEquals(0, metrics.getNotStartedExercises());

        assertTrue(metrics.getExercisesByDifficulty().containsKey("Easy"));
        UserMetricsDTO.ExercisesByDifficultyMetrics diff = metrics.getExercisesByDifficulty().get("Easy");
        assertEquals(1, diff.getTotal());
        assertEquals(1, diff.getCompleted());

        assertFalse(metrics.getExercisesByTopic().isEmpty());
        UserMetricsDTO.ExercisesByTopicMetrics topicMetrics = metrics.getExercisesByTopic().get(0);
        assertEquals("arrays", topicMetrics.getTopicName());
        assertEquals(1, topicMetrics.getTotalExercises());

    }
}
