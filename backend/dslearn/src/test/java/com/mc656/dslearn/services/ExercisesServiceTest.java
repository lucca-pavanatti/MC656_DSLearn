package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.enums.Difficulty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExercisesServiceTest {

    private final ExercisesService exercisesService = new ExercisesService();

    @Test
    void findExercises_shouldReturnOnlyEasyExercises() {
        List<ExerciseDTO> result = exercisesService.findExercises(Difficulty.Easy, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(e -> "Easy".equalsIgnoreCase(e.getDifficulty())));
    }

    @Test
    void findExercises_shouldReturnEmptyListForUnknownDifficulty() {
        // Se Difficulty.Hard não existe nos mockados, deve retornar vazio
        List<ExerciseDTO> result = exercisesService.findExercises(Difficulty.Hard, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findExercises_shouldReturnExercisesForSpecificDataStructure() {
        List<ExerciseDTO> result = exercisesService.findExercises(null, "Array");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(e -> e.getTitle() != null));
    }

    @Test
    void findExercises_shouldReturnAllExercisesWhenNoFilter() {
        List<ExerciseDTO> result = exercisesService.findExercises(null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findExercises_shouldReturnEmptyListForUnknownDataStructure() {
        List<ExerciseDTO> result = exercisesService.findExercises(null, "UnknownStructure");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}