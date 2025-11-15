package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.ExerciseRepository;
import com.mc656.dslearn.validators.ExerciseFilterValidator;
import com.mc656.dslearn.validators.PaginationValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExercisesService {

    private ExerciseMapper exerciseMapper;

    private ExerciseRepository exerciseRepository;

    public ExercisesService(ExerciseMapper exerciseMapper, ExerciseRepository exerciseRepository) {
        this.exerciseMapper = exerciseMapper;
        this.exerciseRepository = exerciseRepository;
    }

    public PagedResponseDTO<ExerciseDTO> findExercisesPaginated(
            String difficulty, 
            String dataStructure, 
            String company,
            int page, 
            int size, 
            String sortBy, 
            String sortDirection) {
        
        // Valida e normaliza os parâmetros usando classes de equivalência
        Difficulty diffEnum = ExerciseFilterValidator.validateDifficulty(difficulty);
        String validatedDataStructure = ExerciseFilterValidator.validateDataStructure(dataStructure);
        String validatedCompany = ExerciseFilterValidator.validateCompany(company);
        
        // Valida parâmetros de paginação usando valores limite
        int validatedPage = PaginationValidator.validatePage(page);
        int validatedSize = PaginationValidator.validateSize(size);
        String validatedSortDirection = PaginationValidator.validateSortDirection(sortDirection);
        String[] allowedFields = {"id", "title", "difficulty"};
        String validatedSortBy = PaginationValidator.validateSortBy(sortBy, allowedFields);

        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(validatedSortDirection)) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, validatedSortBy);
        Pageable pageable = PageRequest.of(validatedPage, validatedSize, sort);

        Page<Exercise> exercisePage = exerciseRepository.findByFiltersPageable(
                diffEnum,
                validatedDataStructure,
                validatedCompany,
                pageable
        );

        List<ExerciseDTO> exerciseDTOs = exerciseMapper.toDtoList(exercisePage.getContent());

        return PagedResponseDTO.<ExerciseDTO>builder()
                .content(exerciseDTOs)
                .page(exercisePage.getNumber())
                .size(exercisePage.getSize())
                .totalElements(exercisePage.getTotalElements())
                .totalPages(exercisePage.getTotalPages())
                .first(exercisePage.isFirst())
                .last(exercisePage.isLast())
                .build();
    }
}
