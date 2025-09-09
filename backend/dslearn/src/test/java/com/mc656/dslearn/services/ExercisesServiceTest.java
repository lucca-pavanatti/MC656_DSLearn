package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
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
                .companies(Arrays.asList("Google", "Facebook", "Amazon"))
                .build();

        mediumStackExerciseDTO = ExerciseDTO.builder()
                .id(150L)
                .title("Evaluate Reverse Polish Notation")
                .url("https://leetcode.com/problems/evaluate-reverse-polish-notation/")
                .difficulty("Medium")
                .companies(Arrays.asList("LinkedIn", "Google"))
                .build();

        easyStackExerciseDTO = ExerciseDTO.builder()
                .id(20L)
                .title("Valid Parentheses")
                .url("https://leetcode.com/problems/valid-parentheses/")
                .difficulty("Easy")
                .companies(Arrays.asList("Google", "Amazon", "Facebook"))
                .build();

        sampleExerciseDTOs = Arrays.asList(easyArrayExerciseDTO, mediumStackExerciseDTO, easyStackExerciseDTO);
    }
    @Test
    void findExercises_shouldReturnOnlyEasyExercises() {
        List<ExerciseDTO> easyExercises = Arrays.asList(easyArrayExerciseDTO, easyStackExerciseDTO);
        when(exerciseMapper.toDtoList(anyList())).thenReturn(easyExercises);

        List<ExerciseDTO> result = exercisesService.findExercises(Difficulty.Easy, null);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(exerciseMapper, times(1)).toDtoList(argThat(exercises -> {
            List<Exercise> exerciseList = (List<Exercise>) exercises;
            return exerciseList.stream().allMatch(ex -> ex.getDifficulty() == Difficulty.Easy);
        }));
    }

    @Test
    void findExercises_shouldReturnEmptyListForHardDifficulty() {
        when(exerciseMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ExerciseDTO> result = exercisesService.findExercises(Difficulty.Hard, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(exerciseMapper, times(1)).toDtoList(argThat(exercises -> {
            List<Exercise> exerciseList = (List<Exercise>) exercises;
            return exerciseList.isEmpty();
        }));
    }

    @Test
    void findExercises_shouldReturnExercisesForSpecificDataStructure() {
        List<ExerciseDTO> arrayExercises = Collections.singletonList(easyArrayExerciseDTO);
        when(exerciseMapper.toDtoList(anyList())).thenReturn(arrayExercises);

        List<ExerciseDTO> result = exercisesService.findExercises(null, "array");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(exerciseMapper, times(1)).toDtoList(argThat(exercises -> {
            List<Exercise> exerciseList = (List<Exercise>) exercises;
            return exerciseList.stream().allMatch(ex ->
                    ex.getRelatedTopics().stream().anyMatch(topic -> topic.equalsIgnoreCase("array"))
            );
        }));
    }

    @Test
    void findExercises_shouldReturnAllExercisesWhenNoFilter() {
        when(exerciseMapper.toDtoList(anyList())).thenReturn(sampleExerciseDTOs);

        List<ExerciseDTO> result = exercisesService.findExercises(null, null);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(exerciseMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void findExercises_shouldReturnEmptyListForUnknownDataStructure() {
        when(exerciseMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ExerciseDTO> result = exercisesService.findExercises(null, "NonExistentStructure");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(exerciseMapper, times(1)).toDtoList(argThat(exercises -> {
            List<Exercise> exerciseList = (List<Exercise>) exercises;
            return exerciseList.isEmpty();
        }));
    }

    @Test
    void testFindExercises_WithBothFilters_ReturnsFilteredExercises() {
        List<ExerciseDTO> filteredExercises = Collections.singletonList(easyStackExerciseDTO);
        when(exerciseMapper.toDtoList(anyList())).thenReturn(filteredExercises);

        List<ExerciseDTO> result = exercisesService.findExercises(Difficulty.Easy, "Stack");

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(exerciseMapper, times(1)).toDtoList(argThat(exercises -> {
            List<Exercise> exerciseList = (List<Exercise>) exercises;
            return exerciseList.stream().allMatch(ex ->
                    ex.getDifficulty() == Difficulty.Easy &&
                            ex.getRelatedTopics().stream().anyMatch(topic -> topic.equalsIgnoreCase("Stack"))
            );
        }));
    }
}