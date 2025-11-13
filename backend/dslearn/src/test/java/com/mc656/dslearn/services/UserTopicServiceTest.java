package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.TopicProgressDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserTopic;
import com.mc656.dslearn.repositories.DSARepository;
import com.mc656.dslearn.repositories.UserRepository;
import com.mc656.dslearn.repositories.UserTopicRepository;
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
class UserTopicServiceTest {

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
    private UserTopic testUserTopic;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        testTopic = DSATopic.builder()
                .id(1L)
                .name("arrays")
                .contentMarkdown("Arrays content")
                .build();

        testUserTopic = UserTopic.builder()
                .id(1L)
                .user(testUser)
                .topic(testTopic)
                .status("started")
                .build();
    }

    @Test
    void updateStatus_WithExistingProgress_ShouldUpdateStatus() {
        String topicName = "arrays";
        String newStatus = "completed";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(testTopic));
        when(userTopicRepository.findByUserIdAndTopicName(1L, topicName)).thenReturn(Optional.of(testUserTopic));
        when(userTopicRepository.save(any(UserTopic.class))).thenReturn(testUserTopic);

        userTopicService.updateStatus(1L, topicName, newStatus);

        verify(userRepository, times(1)).findById(1L);
        verify(dsaRepository, times(1)).findByName(topicName);
        verify(userTopicRepository, times(1)).findByUserIdAndTopicName(1L, topicName);
        verify(userTopicRepository, times(1)).save(testUserTopic);
        assertEquals(newStatus, testUserTopic.getStatus());
    }

    @Test
    void updateStatus_WithNewProgress_ShouldCreateAndSaveProgress() {
        String topicName = "linked-lists";
        String status = "started";
        DSATopic newTopic = DSATopic.builder()
                .id(2L)
                .name(topicName)
                .contentMarkdown("Linked lists content")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.of(newTopic));
        when(userTopicRepository.findByUserIdAndTopicName(1L, topicName)).thenReturn(Optional.empty());
        when(userTopicRepository.save(any(UserTopic.class))).thenAnswer(invocation -> {
            UserTopic saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        userTopicService.updateStatus(1L, topicName, status);

        verify(userRepository, times(1)).findById(1L);
        verify(dsaRepository, times(1)).findByName(topicName);
        verify(userTopicRepository, times(1)).findByUserIdAndTopicName(1L, topicName);
        verify(userTopicRepository, times(1)).save(any(UserTopic.class));
    }

    @Test
    void updateStatus_WithNonExistentUser_ShouldThrowException() {
        Long userId = 999L;
        String topicName = "arrays";
        String status = "started";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userTopicService.updateStatus(userId, topicName, status);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(dsaRepository, never()).findByName(any());
        verify(userTopicRepository, never()).save(any());
    }

    @Test
    void updateStatus_WithNonExistentTopic_ShouldThrowException() {
        String topicName = "nonexistent-topic";
        String status = "started";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(dsaRepository.findByName(topicName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userTopicService.updateStatus(1L, topicName, status);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(dsaRepository, times(1)).findByName(topicName);
        verify(userTopicRepository, never()).save(any());
    }

    @Test
    void getTopicsProgress_WithMultipleTopics_ShouldReturnAllProgress() {
        DSATopic topic2 = DSATopic.builder()
                .id(2L)
                .name("stacks")
                .contentMarkdown("Stacks content")
                .build();

        UserTopic userTopic2 = UserTopic.builder()
                .id(2L)
                .user(testUser)
                .topic(topic2)
                .status("completed")
                .build();

        List<UserTopic> userTopics = Arrays.asList(testUserTopic, userTopic2);

        when(userTopicRepository.findByUserId(1L)).thenReturn(userTopics);

        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("arrays", result.get(0).getName());
        assertEquals("started", result.get(0).getStatus());
        assertEquals("stacks", result.get(1).getName());
        assertEquals("completed", result.get(1).getStatus());

        verify(userTopicRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getTopicsProgress_WithNoProgress_ShouldReturnEmptyList() {
        when(userTopicRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userTopicRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getTopicsProgress_WithSingleTopic_ShouldReturnOneProgress() {
        when(userTopicRepository.findByUserId(1L)).thenReturn(Collections.singletonList(testUserTopic));

        List<TopicProgressDTO> result = userTopicService.getTopicsProgress(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("arrays", result.get(0).getName());
        assertEquals("started", result.get(0).getStatus());

        verify(userTopicRepository, times(1)).findByUserId(1L);
    }
}
