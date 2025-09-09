package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.DataStructureDetailDTO;
import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.mappers.DataStructureMapper;
import com.mc656.dslearn.models.DataStructure;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataStructuresServiceTest {

    @Mock
    private DataStructureMapper dataStructureMapper;

    @InjectMocks
    private DataStructuresService dataStructuresService;

    private DataStructureDetailDTO sampleArrayDTO;
    private DataStructureDetailDTO sampleStackDTO;

    @BeforeEach
    void setUp() {
        List<ExerciseDTO> arrayExercises = List.of(
            ExerciseDTO.builder()
                .id(1L)
                .title("Two Sum")
                .url("https://leetcode.com/problems/two-sum/")
                .difficulty("EASY")
                .companies(List.of("Google", "Facebook", "Amazon", "Microsoft"))
                .build(),
            ExerciseDTO.builder()
                .id(11L)
                .title("Container With Most Water")
                .url("https://leetcode.com/problems/container-with-most-water/")
                .difficulty("MEDIUM")
                .companies(List.of("Google", "Facebook", "Bloomberg"))
                .build(),
            ExerciseDTO.builder()
                .id(15L)
                .title("3Sum")
                .url("https://leetcode.com/problems/3sum/")
                .difficulty("MEDIUM")
                .companies(List.of("Facebook", "Microsoft", "Amazon"))
                .build()
        );

        sampleArrayDTO = DataStructureDetailDTO.builder()
            .name("Array")
            .theory("Um array é uma coleção de itens armazenados em locais de memória contíguos.")
            .exercises(arrayExercises)
            .build();

        List<ExerciseDTO> stackExercises = List.of(
            ExerciseDTO.builder()
                .id(20L)
                .title("Valid Parentheses")
                .url("https://leetcode.com/problems/valid-parentheses/")
                .difficulty("EASY")
                .companies(List.of("Google", "Amazon", "Facebook", "Uber"))
                .build(),
            ExerciseDTO.builder()
                .id(155L)
                .title("Min Stack")
                .url("https://leetcode.com/problems/min-stack/")
                .difficulty("EASY")
                .companies(List.of("Amazon", "Bloomberg", "Uber"))
                .build(),
            ExerciseDTO.builder()
                .id(150L)
                .title("Evaluate Reverse Polish Notation")
                .url("https://leetcode.com/problems/evaluate-reverse-polish-notation/")
                .difficulty("MEDIUM")
                .companies(List.of("LinkedIn", "Google"))
                .build()
        );

        sampleStackDTO = DataStructureDetailDTO.builder()
            .name("Stack")
            .theory("Uma pilha é uma estrutura de dados LIFO (Last-In, First-Out).")
            .exercises(stackExercises)
            .build();
    }

    @Test
    void findDetailsByName_WhenNameIsArray_ShouldReturnArrayDataStructure() {
        when(dataStructureMapper.toDto(any(DataStructure.class))).thenReturn(sampleArrayDTO);

        DataStructureDetailDTO result = dataStructuresService.findDetailsByName("Array");

        assertNotNull(result);
        assertEquals("Array", result.getName());
        assertEquals("Um array é uma coleção de itens armazenados em locais de memória contíguos.", result.getTheory());
        assertEquals(3, result.getExercises().size());
        
        List<ExerciseDTO> exercises = result.getExercises();
        assertEquals("Two Sum", exercises.get(0).getTitle());
        assertEquals("Container With Most Water", exercises.get(1).getTitle());
        assertEquals("3Sum", exercises.get(2).getTitle());
        
        verify(dataStructureMapper, times(1)).toDto(any(DataStructure.class));
    }

    @Test
    void findDetailsByName_WhenNameIsStack_ShouldReturnStackDataStructure() {
        when(dataStructureMapper.toDto(any(DataStructure.class))).thenReturn(sampleStackDTO);

        DataStructureDetailDTO result = dataStructuresService.findDetailsByName("Stack");

        assertNotNull(result);
        assertEquals("Stack", result.getName());
        assertEquals("Uma pilha é uma estrutura de dados LIFO (Last-In, First-Out).", result.getTheory());
        assertEquals(3, result.getExercises().size());
        
        List<ExerciseDTO> exercises = result.getExercises();
        assertEquals("Valid Parentheses", exercises.get(0).getTitle());
        assertEquals("Min Stack", exercises.get(1).getTitle());
        assertEquals("Evaluate Reverse Polish Notation", exercises.get(2).getTitle());
        
        verify(dataStructureMapper, times(1)).toDto(any(DataStructure.class));
    }

    @Test
    void findDetailsByName_WhenNameIsUnknown_ShouldReturnNull() {
        when(dataStructureMapper.toDto((DataStructure) null)).thenReturn(null);

        DataStructureDetailDTO result = dataStructuresService.findDetailsByName("UnknownStructure");

        assertNull(result);
        
        verify(dataStructureMapper, times(1)).toDto((DataStructure) null);
    }

    @Test
    void findDetailsByName_WhenNameIsNull_ShouldReturnNull() {

        DataStructureDetailDTO result = dataStructuresService.findDetailsByName(null);

        assertNull(result);
        
        verify(dataStructureMapper, never()).toDto((DataStructure) null);
    }

    @Test
    void findDetailsByName_WhenNameIsEmpty_ShouldReturnNull() {
        when(dataStructureMapper.toDto((DataStructure) null)).thenReturn(null);

        DataStructureDetailDTO result = dataStructuresService.findDetailsByName("");

        assertNull(result);
        
        verify(dataStructureMapper, times(1)).toDto((DataStructure) null);
    }
}
