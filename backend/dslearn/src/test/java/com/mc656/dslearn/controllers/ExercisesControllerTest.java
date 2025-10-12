package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.services.ExercisesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ExercisesControllerTest {

    @Mock
    private ExercisesService exercisesService;

    @InjectMocks
    private ExercisesController exercisesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exercisesController).build();
    }

    @Test
    void getExercisesByDifficulty_ShouldReturnOnlyEasyExercises() throws Exception {
        List<ExerciseDTO> easyExercises = Arrays.asList(
            ExerciseDTO.builder()
                .id(1L)
                .title("Two Sum")
                .url("https://leetcode.com/problems/two-sum/")
                .difficulty("Easy")
                .companies("Google,Facebook,Amazon")
                .build(),
            ExerciseDTO.builder()
                .id(20L)
                .title("Valid Parentheses")
                .url("https://leetcode.com/problems/valid-parentheses/")
                .difficulty("Easy")
                .companies("Google,Facebook,Amazon")
                .build()
        );

        when(exercisesService.findExercises("Easy", null, null)).thenReturn(easyExercises);

        mockMvc.perform(get("/api/exercises")
                .param("difficulty", "Easy"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].difficulty").value("Easy"))
            .andExpect(jsonPath("$[1].difficulty").value("Easy"));

        verify(exercisesService, times(1)).findExercises("Easy", null, null);
    }
}