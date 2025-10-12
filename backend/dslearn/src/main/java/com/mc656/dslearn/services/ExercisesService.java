package com.mc656.dslearn.services;

import ch.qos.logback.core.util.StringUtil;
import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ExercisesService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<ExerciseDTO> findExercises(String difficulty, String dataStructure, String company) {
        Difficulty diffEnum = null;
        if (difficulty != null && !difficulty.isEmpty()) {
            try {
                diffEnum = Difficulty.valueOf(difficulty);
            } catch (IllegalArgumentException e) {
                // Ignora valor inválido, retorna lista vazia
                return List.of();
            }
        }
        List<Exercise> exercises = exerciseRepository.findByFilters(
                diffEnum,
                (dataStructure == null || dataStructure.isEmpty()) ? null : dataStructure,
                (company == null || company.isEmpty()) ? null : company
        );
        return exerciseMapper.toDtoList(exercises);
    }
}
