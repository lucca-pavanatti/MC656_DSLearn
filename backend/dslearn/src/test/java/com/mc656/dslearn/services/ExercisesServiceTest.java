package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExercisesServiceTest {

    @Mock
    private ExerciseMapper exerciseMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExercisesService exercisesService;

    private List<ExerciseDTO> sampleExerciseDTOs;
    private ExerciseDTO easyArrayExerciseDTO;
    private ExerciseDTO mediumStackExerciseDTO;
    private ExerciseDTO easyStackExerciseDTO;

    @BeforeEach
    void setUp() {
        easyArrayExerciseDTO = ExerciseDTO.builder()
                .id(1L)
                .title("Two Sum")
                .url("https://leetcode.com/problems/two-sum/")
                .difficulty("Easy")
                .companies("Google,Facebook,Amazon")
                .build();

        mediumStackExerciseDTO = ExerciseDTO.builder()
                .id(150L)
                .title("Evaluate Reverse Polish Notation")
                .url("https://leetcode.com/problems/evaluate-reverse-polish-notation/")
                .difficulty("Medium")
                .companies("Google,Facebook,Amazon")
                .build();

        easyStackExerciseDTO = ExerciseDTO.builder()
                .id(20L)
                .title("Valid Parentheses")
                .url("https://leetcode.com/problems/valid-parentheses/")
                .difficulty("Easy")
                .companies("Google,Facebook,Amazon")
                .build();

        sampleExerciseDTOs = Arrays.asList(easyArrayExerciseDTO, mediumStackExerciseDTO, easyStackExerciseDTO);
    }
    @Test
    void findExercises_shouldReturnOnlyEasyExercises() {
        List<Exercise> easyExercises = Arrays.asList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Google,Facebook,Amazon").build()
        );
        when(exerciseRepository.findByFilters(Difficulty.Easy, null, null)).thenReturn(easyExercises);
        when(exerciseMapper.toDtoList(easyExercises)).thenReturn(Arrays.asList(easyArrayExerciseDTO, easyStackExerciseDTO));

        List<ExerciseDTO> result = exercisesService.findExercises("Easy", null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(exerciseRepository, times(1)).findByFilters(Difficulty.Easy, null, null);
        verify(exerciseMapper, times(1)).toDtoList(easyExercises);
    }

    @Test
    void findExercises_shouldReturnEmptyListForHardDifficulty() {
        when(exerciseRepository.findByFilters(Difficulty.Hard, null, null)).thenReturn(Collections.emptyList());
        when(exerciseMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ExerciseDTO> result = exercisesService.findExercises("Hard", null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(exerciseRepository, times(1)).findByFilters(Difficulty.Hard, null, null);
        verify(exerciseMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void findExercises_shouldReturnExercisesForSpecificDataStructure() {
        List<Exercise> arrayExercises = Collections.singletonList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build()
        );
        when(exerciseRepository.findByFilters(null, "array", null)).thenReturn(arrayExercises);
        when(exerciseMapper.toDtoList(arrayExercises)).thenReturn(Collections.singletonList(easyArrayExerciseDTO));

        List<ExerciseDTO> result = exercisesService.findExercises(null, "array", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exerciseRepository, times(1)).findByFilters(null, "array", null);
        verify(exerciseMapper, times(1)).toDtoList(arrayExercises);
    }

    @Test
    void findExercises_shouldReturnAllExercisesWhenNoFilter() {
        List<Exercise> allExercises = Arrays.asList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(150L).difficulty(Difficulty.Medium).relatedTopics("Stack").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Google,Facebook,Amazon").build()
        );
        when(exerciseRepository.findByFilters(null, null, null)).thenReturn(allExercises);
        when(exerciseMapper.toDtoList(allExercises)).thenReturn(sampleExerciseDTOs);

        List<ExerciseDTO> result = exercisesService.findExercises(null, null, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(exerciseRepository, times(1)).findByFilters(null, null, null);
        verify(exerciseMapper, times(1)).toDtoList(allExercises);
    }

    @Test
    void findExercises_shouldReturnEmptyListForUnknownDataStructure() {
        when(exerciseRepository.findByFilters(null, "NonExistentStructure", null)).thenReturn(Collections.emptyList());
        when(exerciseMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ExerciseDTO> result = exercisesService.findExercises(null, "NonExistentStructure", null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(exerciseRepository, times(1)).findByFilters(null, "NonExistentStructure", null);
        verify(exerciseMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void testFindExercises_WithBothFilters_ReturnsFilteredExercises() {
        List<Exercise> filteredExercises = Collections.singletonList(
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Amazon").build()
        );
        when(exerciseRepository.findByFilters(Difficulty.Easy, "Stack", "Amazon")).thenReturn(filteredExercises);
        when(exerciseMapper.toDtoList(filteredExercises)).thenReturn(Collections.singletonList(easyStackExerciseDTO));

        List<ExerciseDTO> result = exercisesService.findExercises("Easy", "Stack", "Amazon");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exerciseRepository, times(1)).findByFilters(Difficulty.Easy, "Stack", "Amazon");
        verify(exerciseMapper, times(1)).toDtoList(filteredExercises);
    }
}