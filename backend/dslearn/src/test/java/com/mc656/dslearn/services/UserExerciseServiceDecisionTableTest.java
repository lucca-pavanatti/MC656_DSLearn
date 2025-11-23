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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tabela de Decisão para UserExerciseService():
 * 
 * Causas:
 * C1: Usuário existe?
 * C2: Exercício existe?
 * C3: UserExercise já existe?
 * 
 * Efeitos:
 * E1: Lançar exceção "Usuário não encontrado"
 * E2: Lançar exceção "Exercício não encontrado"
 * E3: Criar novo UserExercise
 * E4: Atualizar UserExercise existente
 * E5: Salvar UserExercise
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UserExerciseService - Tabela de Decisão")
class UserExerciseServiceDecisionTableTest {

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
    private UserExercise existingUserExercise;

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
                .relatedTopics("Array,Hash Table")
                .companies("Google,Amazon")
                .build();

        existingUserExercise = UserExercise.builder()
                .id(1L)
                .user(testUser)
                .exercise(testExercise)
                .status("in_progress")
                .build();
    }

    // Regra R1: Usuário não existe

    @Test
    @DisplayName("R1: updateStatus com usuário inexistente deve lançar IllegalArgumentException")
    void testUpdateStatus_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 999L;
        Long exerciseId = 1L;
        
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userExerciseService.updateStatus(nonExistentUserId, exerciseId, "completed")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(exerciseRepository, never()).findById(any());
        verify(userExerciseRepository, never()).save(any());
    }

    @Test
    @DisplayName("R1: updateStatus com userId null deve lançar IllegalArgumentException")
    void testUpdateStatus_UserIdNull_ThrowsException() {
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> userExerciseService.updateStatus(null, 1L, "completed")
        );

        verify(exerciseRepository, never()).findById(any());
        verify(userExerciseRepository, never()).save(any());
    }

    // Regra R2: Exercício não existe

    @Test
    @DisplayName("R2: updateStatus com exercício inexistente deve lançar IllegalArgumentException")
    void testUpdateStatus_ExerciseNotFound_ThrowsException() {
        Long userId = 1L;
        Long nonExistentExerciseId = 999L;
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(nonExistentExerciseId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userExerciseService.updateStatus(userId, nonExistentExerciseId, "completed")
        );

        assertEquals("Exercício não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(exerciseRepository, times(1)).findById(nonExistentExerciseId);
        verify(userExerciseRepository, never()).save(any());
    }

    @Test
    @DisplayName("R2: updateStatus com exerciseId null deve lançar IllegalArgumentException")
    void testUpdateStatus_ExerciseIdNull_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(null)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> userExerciseService.updateStatus(1L, null, "completed")
        );

        verify(userRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).findById(null);
        verify(userExerciseRepository, never()).save(any());
    }

    // Regra R3: Criar novo progresso

    @Test
    @DisplayName("R3: updateStatus criando novo UserExercise com status 'completed'")
    void testUpdateStatus_CreateNewProgress_Completed() {
        Long userId = 1L;
        Long exerciseId = 1L;
        String status = "completed";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(userId, exerciseId))
                .thenReturn(Optional.empty());

        userExerciseService.updateStatus(userId, exerciseId, status);

        verify(userExerciseRepository, times(1)).save(argThat(ue -> 
            status.equals(ue.getStatus())
        ));
    }

    @Test
    @DisplayName("R3: updateStatus criando novo UserExercise com status 'in_progress'")
    void testUpdateStatus_CreateNewProgress_InProgress() {
        Long userId = 1L;
        Long exerciseId = 1L;
        String status = "in_progress";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(userId, exerciseId))
                .thenReturn(Optional.empty());

        userExerciseService.updateStatus(userId, exerciseId, status);

        verify(userExerciseRepository, times(1)).save(argThat(ue -> 
            status.equals(ue.getStatus())
        ));
    }

    // Regra R4: Atualizar progresso existente

    @Test
    @DisplayName("R4: updateStatus atualizando UserExercise existente de 'in_progress' para 'completed'")
    void testUpdateStatus_UpdateExistingProgress_InProgressToCompleted() {
        Long userId = 1L;
        Long exerciseId = 1L;
        String newStatus = "completed";

        existingUserExercise.setStatus("in_progress");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(userId, exerciseId))
                .thenReturn(Optional.of(existingUserExercise));

        userExerciseService.updateStatus(userId, exerciseId, newStatus);

        verify(userExerciseRepository, times(1)).save(argThat(ue -> 
            newStatus.equals(ue.getStatus())
        ));
    }

    @Test
    @DisplayName("R4: updateStatus mantendo mesmo status não deve falhar")
    void testUpdateStatus_UpdateExistingProgress_SameStatus() {
        Long userId = 1L;
        Long exerciseId = 1L;
        String sameStatus = "completed";

        existingUserExercise.setStatus(sameStatus);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(testExercise));
        when(userExerciseRepository.findByUserIdAndExerciseId(userId, exerciseId))
                .thenReturn(Optional.of(existingUserExercise));

        userExerciseService.updateStatus(userId, exerciseId, sameStatus);

        verify(userExerciseRepository, times(1)).save(argThat(ue -> 
            sameStatus.equals(ue.getStatus())
        ));
    }

    // Testes para getExercisesProgress()

    @Test
    @DisplayName("getExercisesProgress deve retornar lista vazia para usuário sem progresso")
    void testGetExercisesProgress_NoProgress_ReturnsEmptyList() {
        Long userId = 1L;
        when(userExerciseRepository.findByUserId(userId)).thenReturn(List.of());

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userExerciseRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getExercisesProgress deve retornar lista com um exercício")
    void testGetExercisesProgress_OneExercise_ReturnsOneItem() {
        Long userId = 1L;
        List<UserExercise> userExercises = Arrays.asList(existingUserExercise);
        
        when(userExerciseRepository.findByUserId(userId)).thenReturn(userExercises);

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testExercise.getId(), result.get(0).getId());
        assertEquals(testExercise.getTitle(), result.get(0).getTitle());
        assertEquals(existingUserExercise.getStatus(), result.get(0).getStatus());
        verify(userExerciseRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getExercisesProgress deve retornar lista com múltiplos exercícios")
    void testGetExercisesProgress_MultipleExercises_ReturnsMultipleItems() {
        Long userId = 1L;
        
        Exercise exercise2 = Exercise.builder()
                .id(2L)
                .title("Valid Parentheses")
                .difficulty(Difficulty.Easy)
                .build();

        UserExercise userExercise2 = UserExercise.builder()
                .id(2L)
                .user(testUser)
                .exercise(exercise2)
                .status("started")
                .build();

        List<UserExercise> userExercises = Arrays.asList(existingUserExercise, userExercise2);
        when(userExerciseRepository.findByUserId(userId)).thenReturn(userExercises);

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userExerciseRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getExercisesProgress deve converter corretamente os status dos exercícios")
    void testGetExercisesProgress_ConvertsStatusCorrectly() {
        Long userId = 1L;
        
        Exercise exercise2 = Exercise.builder()
                .id(2L)
                .title("Exercise 2")
                .difficulty(Difficulty.Medium)
                .build();

        Exercise exercise3 = Exercise.builder()
                .id(3L)
                .title("Exercise 3")
                .difficulty(Difficulty.Hard)
                .build();

        UserExercise ue1 = UserExercise.builder()
                .id(1L)
                .user(testUser)
                .exercise(testExercise)
                .status("completed")
                .build();

        UserExercise ue2 = UserExercise.builder()
                .id(2L)
                .user(testUser)
                .exercise(exercise2)
                .status("in_progress")
                .build();

        UserExercise ue3 = UserExercise.builder()
                .id(3L)
                .user(testUser)
                .exercise(exercise3)
                .status("started")
                .build();

        List<UserExercise> userExercises = Arrays.asList(ue1, ue2, ue3);
        when(userExerciseRepository.findByUserId(userId)).thenReturn(userExercises);

        List<ExerciseProgressDTO> result = userExerciseService.getExercisesProgress(userId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("completed", result.get(0).getStatus());
        assertEquals("in_progress", result.get(1).getStatus());
        assertEquals("started", result.get(2).getStatus());
    }
}
