package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void findExercisesPaginated_shouldReturnOnlyEasyExercises() {
        List<Exercise> easyExercises = Arrays.asList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Google,Facebook,Amazon").build()
        );
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> exercisePage = new PageImpl<>(easyExercises, pageable, easyExercises.size());
        
        when(exerciseRepository.findByFiltersPageable(eq(Difficulty.Easy), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(exercisePage);
        when(exerciseMapper.toDtoList(easyExercises)).thenReturn(Arrays.asList(easyArrayExerciseDTO, easyStackExerciseDTO));

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated("Easy", null, null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(2, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(eq(Difficulty.Easy), isNull(), isNull(), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(easyExercises);
    }

    @Test
    void findExercisesPaginated_shouldReturnEmptyListForHardDifficulty() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        when(exerciseRepository.findByFiltersPageable(eq(Difficulty.Hard), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(emptyPage);
        when(exerciseMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated("Hard", null, null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(eq(Difficulty.Hard), isNull(), isNull(), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void findExercisesPaginated_shouldReturnExercisesForSpecificDataStructure() {
        List<Exercise> arrayExercises = Collections.singletonList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build()
        );
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> exercisePage = new PageImpl<>(arrayExercises, pageable, arrayExercises.size());
        
        when(exerciseRepository.findByFiltersPageable(isNull(), eq("array"), isNull(), any(Pageable.class)))
                .thenReturn(exercisePage);
        when(exerciseMapper.toDtoList(arrayExercises)).thenReturn(Collections.singletonList(easyArrayExerciseDTO));

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated(null, "array", null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(isNull(), eq("array"), isNull(), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(arrayExercises);
    }

    @Test
    void findExercisesPaginated_shouldReturnAllExercisesWhenNoFilter() {
        List<Exercise> allExercises = Arrays.asList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(150L).difficulty(Difficulty.Medium).relatedTopics("Stack").companies("Google,Facebook,Amazon").build(),
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Google,Facebook,Amazon").build()
        );
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> exercisePage = new PageImpl<>(allExercises, pageable, allExercises.size());
        
        when(exerciseRepository.findByFiltersPageable(isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(exercisePage);
        when(exerciseMapper.toDtoList(allExercises)).thenReturn(sampleExerciseDTOs);

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated(null, null, null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(isNull(), isNull(), isNull(), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(allExercises);
    }

    @Test
    void findExercisesPaginated_shouldReturnEmptyListForUnknownDataStructure() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        when(exerciseRepository.findByFiltersPageable(isNull(), eq("NonExistentStructure"), isNull(), any(Pageable.class)))
                .thenReturn(emptyPage);
        when(exerciseMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated(null, "NonExistentStructure", null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(isNull(), eq("NonExistentStructure"), isNull(), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void testFindExercisesPaginated_WithBothFilters_ReturnsFilteredExercises() {
        List<Exercise> filteredExercises = Collections.singletonList(
                Exercise.builder().id(20L).difficulty(Difficulty.Easy).relatedTopics("Stack").companies("Amazon").build()
        );
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exercise> exercisePage = new PageImpl<>(filteredExercises, pageable, filteredExercises.size());
        
        when(exerciseRepository.findByFiltersPageable(eq(Difficulty.Easy), eq("Stack"), eq("Amazon"), any(Pageable.class)))
                .thenReturn(exercisePage);
        when(exerciseMapper.toDtoList(filteredExercises)).thenReturn(Collections.singletonList(easyStackExerciseDTO));

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated("Easy", "Stack", "Amazon", 0, 10, "id", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        verify(exerciseRepository, times(1)).findByFiltersPageable(eq(Difficulty.Easy), eq("Stack"), eq("Amazon"), any(Pageable.class));
        verify(exerciseMapper, times(1)).toDtoList(filteredExercises);
    }

    @Test
    void findExercisesPaginated_shouldReturnEmptyPageForInvalidDifficulty() {
        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated("InvalidDifficulty", null, null, 0, 10, "id", "asc");

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
        
        // Verify that repository was not called for invalid difficulty
        verify(exerciseRepository, never()).findByFiltersPageable(any(), any(), any(), any());
        verify(exerciseMapper, never()).toDtoList(any());
    }

    @Test
    void findExercisesPaginated_shouldHandleCustomPaginationAndSorting() {
        List<Exercise> exercises = Collections.singletonList(
                Exercise.builder().id(1L).difficulty(Difficulty.Easy).relatedTopics("Array").companies("Google").build()
        );
        
        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "title"));
        Page<Exercise> exercisePage = new PageImpl<>(exercises, pageable, 10);
        
        when(exerciseRepository.findByFiltersPageable(isNull(), isNull(), isNull(), any(Pageable.class)))
                .thenReturn(exercisePage);
        when(exerciseMapper.toDtoList(exercises)).thenReturn(Collections.singletonList(easyArrayExerciseDTO));

        PagedResponseDTO<ExerciseDTO> result = exercisesService.findExercisesPaginated(null, null, null, 1, 5, "title", "desc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getPage());
        assertEquals(5, result.getSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertFalse(result.isFirst());
        assertTrue(result.isLast());
        
        verify(exerciseRepository, times(1)).findByFiltersPageable(isNull(), isNull(), isNull(), 
                argThat(p -> p.getPageNumber() == 1 && p.getPageSize() == 5 && 
                            p.getSort().getOrderFor("title").getDirection() == Sort.Direction.DESC));
    }
}