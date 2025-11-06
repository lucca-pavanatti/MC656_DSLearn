package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.services.DSATopicsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DSATopicsControllerTest {

    @Mock
    private DSATopicsService dataStructureService;

    @InjectMocks
    private DSATopicsController dataStructuresController;

    private MockMvc mockMvc;
    private DSADetailDTO sampleDataStructureDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataStructuresController).build();

        sampleDataStructureDTO = DSADetailDTO.builder()
            .name("array")
            .contentMarkdown("Arrays são estruturas de dados que armazenam elementos do mesmo tipo em posições contíguas de memória.")
            .build();
    }
    @Test
    void getStructureById_WithMockMvc_WhenDataStructureExists_ShouldReturnOkWithJson() throws Exception {
        String structureName = "array";
        when(dataStructureService.findDetailsByName(structureName)).thenReturn(sampleDataStructureDTO);

        mockMvc.perform(get("/api/topics/{name}", structureName))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").value("array"))
            .andExpect(jsonPath("$.contentMarkdown").value("Arrays são estruturas de dados que armazenam elementos do mesmo tipo em posições contíguas de memória."));

        verify(dataStructureService, times(1)).findDetailsByName(structureName);
    }

    @Test
    void getStructureById_WithMockMvc_WhenDataStructureDoesNotExist_ShouldReturnNotFound() throws Exception {
        String structureName = "nonexistent";
        when(dataStructureService.findDetailsByName(structureName)).thenReturn(null);

        mockMvc.perform(get("/api/topics/{name}", structureName))
            .andExpect(status().isNotFound());

        verify(dataStructureService, times(1)).findDetailsByName(structureName);
    }
}
