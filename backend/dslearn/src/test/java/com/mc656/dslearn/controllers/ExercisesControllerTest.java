package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.services.ExercisesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        PagedResponseDTO<ExerciseDTO> pagedResponse = PagedResponseDTO.<ExerciseDTO>builder()
                .content(easyExercises)
                .page(0)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(exercisesService.findExercisesPaginated(eq("Easy"), eq(null), eq(null), 
                eq(0), eq(10), eq("id"), eq("asc"))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/exercises")
                .param("difficulty", "Easy"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].difficulty").value("Easy"))
            .andExpect(jsonPath("$.content[1].difficulty").value("Easy"))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.first").value(true))
            .andExpect(jsonPath("$.last").value(true));

        verify(exercisesService, times(1)).findExercisesPaginated("Easy", null, null, 0, 10, "id", "asc");
    }
}