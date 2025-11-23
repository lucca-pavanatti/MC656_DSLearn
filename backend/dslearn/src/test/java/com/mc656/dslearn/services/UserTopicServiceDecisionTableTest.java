package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.TopicProgressDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserTopic;
import com.mc656.dslearn.repositories.DSARepository;
import com.mc656.dslearn.repositories.UserRepository;
import com.mc656.dslearn.repositories.UserTopicRepository;
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
 * Tabela de Decisão para UserTopicService:
 * 
 * Causas:
 * C1: Usuário existe?
 * C2: Tópico DSA existe?
 * C3: UserTopic já existe?
 * 
 * Efeitos:
 * E1: Lançar exceção "Usuário não encontrado"
 * E2: Lançar exceção "Tópico não encontrado"
 * E3: Criar novo UserTopic
 * E4: Atualizar UserTopic existente
 * E5: Salvar UserTopic
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de UserTopicService - Tabela de Decisão")
class UserTopicServiceDecisionTableTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTopicRepository userTopicRepository;

    @Mock
    private DSARepository dsaRepository;

    @InjectMocks
    private UserTopicService userTopicService;

    private User testUser;
    private DSATopic testTopic;
    private UserTopic existingUserTopic;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        testTopic = DSATopic.builder()
                .id(1L)
                .name("Arrays")
                .contentMarkdown("# Arrays\n\nArrays são estruturas de dados...")
                .build();

        existingUserTopic = UserTopic.builder()
                .id(1L)
                .user(testUser)
                .topic(testTopic)
                .status("in_progress")
                .build();
    }

    // ========== REGRA R1: Usuário não existe ==========

    @Test
    @DisplayName("R1: updateStatus com usuário inexistente deve lançar IllegalArgumentException")
    void testUpdateStatus_UserNotFound_ThrowsException() {
        // Arrange
        Long nonExistentUserId = 999L;
        String topicName = "Arrays";
        
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userTopicService.updateStatus(nonExistentUserId, topicName, "completed")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(dsaRepository, never()).findByName(any());
        verify(userTopicRepository, never()).save(any());
    }

    @Test
    @DisplayName("R1: updateStatus com userId null deve lançar IllegalArgumentException")
    void testUpdateStatus_UserIdNull_ThrowsException() {
        // Arrange
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> userTopicService.updateStatus(null, "Arrays", "completed")
        );

        verify(dsaRepository, never()).findByName(any());
        verify(userTopicRepository, never()).save(any());
    }

    // ========== REGRA R2: Tópico não existe ==========

    @Test
    @DisplayName("R2: updateStatus com tópico inexistente deve lançar IllegalArgumentException")
    void testUpdateStatus_TopicNotFound_ThrowsException() {
        // Arrange
        Long userId = 1L;
        String nonExistentTopic = "NonExistentTopic";
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(nonExistentTopic)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userTopicService.updateStatus(userId, nonExistentTopic, "completed")
        );

        assertEquals("Tópico não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(dsaRepository, times(1)).findByName(nonExistentTopic);
        verify(userTopicRepository, never()).save(any());
    }

    @Test
    @DisplayName("R2: updateStatus com topicName null deve lançar IllegalArgumentException")
    void testUpdateStatus_TopicNameNull_ThrowsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> userTopicService.updateStatus(1L, null, "completed")
        );

        verify(userRepository, times(1)).findById(1L);
        verify(dsaRepository, times(1)).findByName(null);
        verify(userTopicRepository, never()).save(any());
    }

    @Test
    @DisplayName("R2: updateStatus com topicName vazio deve lançar IllegalArgumentException")
    void testUpdateStatus_TopicNameEmpty_ThrowsException() {
        // Arrange
        String emptyTopic = "";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(emptyTopic)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> userTopicService.updateStatus(1L, emptyTopic, "completed")
        );

        verify(userRepository, times(1)).findById(1L);
        verify(dsaRepository, times(1)).findByName(emptyTopic);
        verify(userTopicRepository, never()).save(any());
    }

    // ========== REGRA R3: Criar novo progresso ==========

    @Test
    @DisplayName("R3: updateStatus criando novo UserTopic com status 'started'")
    void testUpdateStatus_CreateNewProgress_Started() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String status = "started";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.empty());
        
        UserTopic newUserTopic = new UserTopic(testUser, testTopic);
        when(userTopicRepository.save(any(UserTopic.class))).thenReturn(newUserTopic);

        // Act
        userTopicService.updateStatus(userId, topicName, status);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(dsaRepository, times(1)).findByName(topicName);
        verify(userTopicRepository, times(1)).findByUserIdAndTopicName(userId, topicName);
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            ut.getUser().equals(testUser) &&
            ut.getTopic().equals(testTopic) &&
            status.equals(ut.getStatus())
        ));
    }

    @Test
    @DisplayName("R3: updateStatus criando novo UserTopic com status 'completed'")
    void testUpdateStatus_CreateNewProgress_Completed() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String status = "completed";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.empty());

        // Act
        userTopicService.updateStatus(userId, topicName, status);

        // Assert
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            status.equals(ut.getStatus())
        ));
    }

    @Test
    @DisplayName("R3: updateStatus criando novo UserTopic com status 'in_progress'")
    void testUpdateStatus_CreateNewProgress_InProgress() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String status = "in_progress";

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.empty());

        // Act
        userTopicService.updateStatus(userId, topicName, status);

        // Assert
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            status.equals(ut.getStatus())
        ));
    }

    // ========== REGRA R4: Atualizar progresso existente ==========

    @Test
    @DisplayName("R4: updateStatus atualizando UserTopic existente de 'started' para 'completed'")
    void testUpdateStatus_UpdateExistingProgress_StartedToCompleted() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String newStatus = "completed";

        existingUserTopic.setStatus("started");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.of(existingUserTopic));
        when(userTopicRepository.save(any(UserTopic.class))).thenReturn(existingUserTopic);

        // Act
        userTopicService.updateStatus(userId, topicName, newStatus);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(dsaRepository, times(1)).findByName(topicName);
        verify(userTopicRepository, times(1)).findByUserIdAndTopicName(userId, topicName);
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            ut.getId().equals(existingUserTopic.getId()) &&
            newStatus.equals(ut.getStatus())
        ));
    }

    @Test
    @DisplayName("R4: updateStatus atualizando UserTopic existente de 'in_progress' para 'completed'")
    void testUpdateStatus_UpdateExistingProgress_InProgressToCompleted() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String newStatus = "completed";

        existingUserTopic.setStatus("in_progress");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.of(existingUserTopic));

        // Act
        userTopicService.updateStatus(userId, topicName, newStatus);

        // Assert
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            newStatus.equals(ut.getStatus())
        ));
    }

    @Test
    @DisplayName("R4: updateStatus atualizando UserTopic existente de 'completed' para 'started'")
    void testUpdateStatus_UpdateExistingProgress_CompletedToStarted() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String newStatus = "started";

        existingUserTopic.setStatus("completed");

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.of(existingUserTopic));

        // Act
        userTopicService.updateStatus(userId, topicName, newStatus);

        // Assert
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            newStatus.equals(ut.getStatus())
        ));
    }

    @Test
    @DisplayName("R4: updateStatus mantendo mesmo status não deve falhar")
    void testUpdateStatus_UpdateExistingProgress_SameStatus() {
        // Arrange
        Long userId = 1L;
        String topicName = "Arrays";
        String sameStatus = "completed";

        existingUserTopic.setStatus(sameStatus);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(userId, topicName))
                .thenReturn(Optional.of(existingUserTopic));

        // Act
        userTopicService.updateStatus(userId, topicName, sameStatus);

        // Assert
        verify(userTopicRepository, times(1)).save(argThat(ut -> 
            sameStatus.equals(ut.getStatus())
        ));
    }

    // ========== TESTES PARA getTopicsProgress() ==========

    @Test
    @DisplayName("getTopicsProgress deve retornar lista vazia para usuário sem progresso")
    void testGetTopicsProgress_NoProgress_ReturnsEmptyList() {
        // Arrange
        Long userId = 1L;
        when(userTopicRepository.findByUserId(userId)).thenReturn(List.of());

        // Act
        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userTopicRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getTopicsProgress deve retornar lista com um tópico")
    void testGetTopicsProgress_OneTopic_ReturnsOneItem() {
        // Arrange
        Long userId = 1L;
        List<UserTopic> userTopics = Arrays.asList(existingUserTopic);
        
        when(userTopicRepository.findByUserId(userId)).thenReturn(userTopics);

        // Act
        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTopic.getName(), result.get(0).getName());
        assertEquals(existingUserTopic.getStatus(), result.get(0).getStatus());
        verify(userTopicRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getTopicsProgress deve retornar lista com múltiplos tópicos")
    void testGetTopicsProgress_MultipleTopics_ReturnsMultipleItems() {
        // Arrange
        Long userId = 1L;
        
        DSATopic topic2 = DSATopic.builder()
                .id(2L)
                .name("Linked Lists")
                .contentMarkdown("# Linked Lists\n\nListas encadeadas...")
                .build();

        UserTopic userTopic2 = UserTopic.builder()
                .id(2L)
                .user(testUser)
                .topic(topic2)
                .status("started")
                .build();

        List<UserTopic> userTopics = Arrays.asList(existingUserTopic, userTopic2);
        when(userTopicRepository.findByUserId(userId)).thenReturn(userTopics);

        // Act
        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userTopicRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("getTopicsProgress deve converter corretamente os status dos tópicos")
    void testGetTopicsProgress_ConvertsStatusCorrectly() {
        // Arrange
        Long userId = 1L;
        
        DSATopic topic2 = DSATopic.builder()
                .id(2L)
                .name("Trees")
                .contentMarkdown("# Trees")
                .build();

        DSATopic topic3 = DSATopic.builder()
                .id(3L)
                .name("Graphs")
                .contentMarkdown("# Graphs")
                .build();

        UserTopic ut1 = UserTopic.builder()
                .id(1L)
                .user(testUser)
                .topic(testTopic)
                .status("completed")
                .build();

        UserTopic ut2 = UserTopic.builder()
                .id(2L)
                .user(testUser)
                .topic(topic2)
                .status("in_progress")
                .build();

        UserTopic ut3 = UserTopic.builder()
                .id(3L)
                .user(testUser)
                .topic(topic3)
                .status("started")
                .build();

        List<UserTopic> userTopics = Arrays.asList(ut1, ut2, ut3);
        when(userTopicRepository.findByUserId(userId)).thenReturn(userTopics);

        // Act
        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(userId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("completed", result.get(0).getStatus());
        assertEquals("in_progress", result.get(1).getStatus());
        assertEquals("started", result.get(2).getStatus());
    }
}
