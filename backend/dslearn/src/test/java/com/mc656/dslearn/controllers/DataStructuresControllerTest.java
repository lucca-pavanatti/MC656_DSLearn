package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.DataStructureDetailDTO;
import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.services.DataStructuresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DataStructuresControllerTest {

    @Mock
    private DataStructuresService dataStructureService;

    @InjectMocks
    private DataStructuresController dataStructuresController;

    private MockMvc mockMvc;
    private DataStructureDetailDTO sampleDataStructureDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataStructuresController).build();
        
        List<ExerciseDTO> exercises = Arrays.asList(
            ExerciseDTO.builder()
                .id(1L)
                .title("Two Sum")
                .url("https://leetcode.com/problems/two-sum/")
                .difficulty("Easy")
                .companies("Amazon,Google,Microsoft")
                .build(),
            ExerciseDTO.builder()
                .id(2L)
                .title("Valid Parentheses")
                .url("https://leetcode.com/problems/valid-parentheses/")
                .difficulty("Easy")
                .companies("Facebook,Amazon")
                .build()
        );

        sampleDataStructureDTO = DataStructureDetailDTO.builder()
            .name("array")
            .theory("Arrays são estruturas de dados que armazenam elementos do mesmo tipo em posições contíguas de memória.")
            .exercises(exercises)
            .build();
    }
    @Test
    void getStructureById_WithMockMvc_WhenDataStructureExists_ShouldReturnOkWithJson() throws Exception {
        String structureName = "array";
        when(dataStructureService.findDetailsByName(structureName)).thenReturn(sampleDataStructureDTO);

        mockMvc.perform(get("/api/data-structures/{name}", structureName))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").value("array"))
            .andExpect(jsonPath("$.theory").value("Arrays são estruturas de dados que armazenam elementos do mesmo tipo em posições contíguas de memória."))
            .andExpect(jsonPath("$.exercises").isArray())
            .andExpect(jsonPath("$.exercises").isNotEmpty())
            .andExpect(jsonPath("$.exercises[0].id").value(1))
            .andExpect(jsonPath("$.exercises[0].title").value("Two Sum"))
            .andExpect(jsonPath("$.exercises[0].difficulty").value("Easy"))
            .andExpect(jsonPath("$.exercises[1].id").value(2))
            .andExpect(jsonPath("$.exercises[1].title").value("Valid Parentheses"));

        verify(dataStructureService, times(1)).findDetailsByName(structureName);
    }

    @Test
    void getStructureById_WithMockMvc_WhenDataStructureDoesNotExist_ShouldReturnNotFound() throws Exception {
        String structureName = "nonexistent";
        when(dataStructureService.findDetailsByName(structureName)).thenReturn(null);

        mockMvc.perform(get("/api/data-structures/{name}", structureName))
            .andExpect(status().isNotFound());

        verify(dataStructureService, times(1)).findDetailsByName(structureName);
    }
}
