package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.mappers.DataStructureMapper;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.repositories.DSARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DSATopicsServiceTest {

    @Mock
    private DataStructureMapper dataStructureMapper;

    @Mock
    private DSARepository dsaRepository;

    @InjectMocks
    private DSATopicsService dataStructuresService;

    private DSADetailDTO sampleArrayDTO;
    private DSADetailDTO sampleStackDTO;

    @BeforeEach
    void setUp() {
        sampleArrayDTO = DSADetailDTO.builder()
            .name("Array")
            .contentMarkdown("Um array é uma coleção de itens armazenados em locais de memória contíguos.")
            .build();

        sampleStackDTO = DSADetailDTO.builder()
            .name("Stack")
            .contentMarkdown("Uma pilha é uma estrutura de dados LIFO (Last-In, First-Out).")
            .build();
    }

    @Test
    void findDetailsByName_WhenNameIsArray_ShouldReturnArrayDataStructure() {
        DSATopic dummyTopic = new DSATopic();
        when(dsaRepository.findByName("Array")).thenReturn(dummyTopic);
        when(dataStructureMapper.toDto(dummyTopic)).thenReturn(sampleArrayDTO);

        DSADetailDTO result = dataStructuresService.findDetailsByName("Array");

        assertNotNull(result);
        assertEquals("Array", result.getName());
        assertEquals("Um array é uma coleção de itens armazenados em locais de memória contíguos.", result.getContentMarkdown());
        verify(dataStructureMapper, times(1)).toDto(dummyTopic);
    }

    @Test
    void findDetailsByName_WhenNameIsStack_ShouldReturnStackDataStructure() {
        DSATopic dummyTopic = new DSATopic();
        when(dsaRepository.findByName("Stack")).thenReturn(dummyTopic);
        when(dataStructureMapper.toDto(dummyTopic)).thenReturn(sampleStackDTO);

        DSADetailDTO result = dataStructuresService.findDetailsByName("Stack");

        assertNotNull(result);
        assertEquals("Stack", result.getName());
        assertEquals("Uma pilha é uma estrutura de dados LIFO (Last-In, First-Out).", result.getContentMarkdown());
        verify(dataStructureMapper, times(1)).toDto(dummyTopic);
    }

    @Test
    void findDetailsByName_WhenNameIsUnknown_ShouldThrowNullPointerException() {
        when(dsaRepository.findByName("UnknownStructure")).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            dataStructuresService.findDetailsByName("UnknownStructure");
        });
        assertEquals("DSA Topic not found: UnknownStructure", exception.getMessage());
        verify(dataStructureMapper, never()).toDto((DSATopic) any());
    }

    @Test
    void findDetailsByName_WhenNameIsNull_ShouldReturnNull() {
        DSADetailDTO result = dataStructuresService.findDetailsByName(null);

        assertNull(result);
        verify(dataStructureMapper, never()).toDto((DSATopic) null);
    }

    @Test
    void findDetailsByName_WhenNameIsEmpty_ShouldThrowNullPointerException() {
        when(dsaRepository.findByName("")).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            dataStructuresService.findDetailsByName("");
        });
        assertEquals("DSA Topic not found: ", exception.getMessage());
        verify(dataStructureMapper, never()).toDto((DSATopic) any());
    }
}
