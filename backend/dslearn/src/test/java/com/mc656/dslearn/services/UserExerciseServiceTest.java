package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseProgressDTO;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserExercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.ExerciseRepository;
import com.mc656.dslearn.repositories.UserExerciseRepository;
import com.mc656.dslearn.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserExerciseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserExerciseRepository userExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private UserExerciseService userExerciseService;

    private User testUser;
    private Exercise testExercise;
    private UserExercise testUserExercise;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        testExercise = Exercise.builder()
                .id(1L)
                .title("Two Sum")
                .url("https://leetcode.com/problems/two-sum/")
                .difficulty(Difficulty.Easy)
                .relatedTopics("arrays,hash-table")
                .companies("Google,Amazon")
                .build();

        testUserExercise = UserExercise.builder()
                .id(1L)
                .user(testUser)
                .exercise(testExercise)
                .status("started")
                .build();
    }

    @Test
    void updateStatus_WithExistingProgress_ShouldUpdateStatus() {
        Long exerciseId = 1L;
        String newStatus = "completed";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(1L, exerciseId)).thenReturn(Optional.of(testUserExercise));
        when(userExerciseRepository.save(any(UserExercise.class))).thenReturn(testUserExercise);

        userExerciseService.updateStatus(1L, exerciseId, newStatus);

        verify(userRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(userExerciseRepository, times(1)).findByUserIdAndExerciseId(1L, exerciseId);
        verify(userExerciseRepository, times(1)).save(testUserExercise);
        assertEquals(newStatus, testUserExercise.getStatus());
    }

    @Test
    void updateStatus_WithNewProgress_ShouldCreateAndSaveProgress() {
        Long exerciseId = 2L;
        String status = "started";
        Exercise newExercise = Exercise.builder()
                .id(exerciseId)
                .title("Add Two Numbers")
                .url("https://leetcode.com/problems/add-two-numbers/")
                .difficulty(Difficulty.Medium)
                .relatedTopics("linked-list,math")
                .companies("Microsoft,Facebook")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(newExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(1L, exerciseId)).thenReturn(Optional.empty());
        when(userExerciseRepository.save(any(UserExercise.class))).thenAnswer(invocation -> {
            UserExercise saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        userExerciseService.updateStatus(1L, exerciseId, status);

        verify(userRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(userExerciseRepository, times(1)).findByUserIdAndExerciseId(1L, exerciseId);
        verify(userExerciseRepository, times(1)).save(any(UserExercise.class));
    }

    @Test
    void updateStatus_WithNonExistentUser_ShouldThrowException() {
        Long userId = 999L;
        Long exerciseId = 1L;
        String status = "started";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userExerciseService.updateStatus(userId, exerciseId, status);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(exerciseRepository, never()).findById(any());
        verify(userExerciseRepository, never()).save(any());
    }

    @Test
    void updateStatus_WithNonExistentExercise_ShouldThrowException() {
        Long exerciseId = 999L;
        String status = "started";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userExerciseService.updateStatus(1L, exerciseId, status);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).findById(exerciseId);
        verify(userExerciseRepository, never()).save(any());
    }

    @Test
    void getExercisesProgress_WithMultipleExercises_ShouldReturnAllProgress() {
        Exercise exercise2 = Exercise.builder()
                .id(2L)
                .title("Reverse Linked List")
                .url("https://leetcode.com/problems/reverse-linked-list/")
                .difficulty(Difficulty.Easy)
                .relatedTopics("linked-list")
                .companies("Amazon,Apple")
                .build();

        UserExercise userExercise2 = UserExercise.builder()
                .id(2L)
                .user(testUser)
                .exercise(exercise2)
                .status("completed")
                .build();

        List<UserExercise> userExercises = Arrays.asList(testUserExercise, userExercise2);

        when(userExerciseRepository.findByUserId(1L)).thenReturn(userExercises);

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Two Sum", result.get(0).getTitle());
        assertEquals("started", result.get(0).getStatus());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Reverse Linked List", result.get(1).getTitle());
        assertEquals("completed", result.get(1).getStatus());

        verify(userExerciseRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getExercisesProgress_WithNoProgress_ShouldReturnEmptyList() {
        when(userExerciseRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userExerciseRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getExercisesProgress_WithSingleExercise_ShouldReturnOneProgress() {
        when(userExerciseRepository.findByUserId(1L)).thenReturn(Collections.singletonList(testUserExercise));

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Two Sum", result.get(0).getTitle());
        assertEquals("started", result.get(0).getStatus());

        verify(userExerciseRepository, times(1)).findByUserId(1L);
    }

    @Test
    void updateStatus_ChangingFromStartedToCompleted_ShouldUpdateCorrectly() {
        Long exerciseId = 1L;
        String newStatus = "completed";

        testUserExercise.setStatus("started");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(1L, exerciseId)).thenReturn(Optional.of(testUserExercise));
        when(userExerciseRepository.save(any(UserExercise.class))).thenReturn(testUserExercise);

        userExerciseService.updateStatus(1L, exerciseId, newStatus);

        assertEquals("completed", testUserExercise.getStatus());
        verify(userExerciseRepository, times(1)).save(testUserExercise);
    }
}
