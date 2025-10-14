package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExercisesService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public PagedResponseDTO<ExerciseDTO> findExercisesPaginated(
            String difficulty, 
            String dataStructure, 
            String company,
            int page, 
            int size, 
            String sortBy, 
            String sortDirection) {
        
        Difficulty diffEnum = null;
        if (difficulty != null && !difficulty.isEmpty()) {
            try {
                diffEnum = Difficulty.valueOf(difficulty);
            } catch (IllegalArgumentException e) {
                // Retorna página vazia para valores inválidos
                return new PagedResponseDTO<>(
                    List.of(), page, size, 0, 0, true, true
                );
            }
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            direction = Sort.Direction.DESC;
        }

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Exercise> exercisePage = exerciseRepository.findByFiltersPageable(
                diffEnum,
                (dataStructure == null || dataStructure.isEmpty()) ? null : dataStructure,
                (company == null || company.isEmpty()) ? null : company,
                pageable
        );

        List<ExerciseDTO> exerciseDTOs = exerciseMapper.toDtoList(exercisePage.getContent());

        return new PagedResponseDTO<>(
                exerciseDTOs,
                exercisePage.getNumber(),
                exercisePage.getSize(),
                exercisePage.getTotalElements(),
                exercisePage.getTotalPages(),
                exercisePage.isFirst(),
                exercisePage.isLast()
        );
    }
}
