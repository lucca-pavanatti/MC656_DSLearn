package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.services.ExercisesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
public class ExercisesController {

    private ExercisesService exerciseService;

    public ExercisesController(ExercisesService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<ExerciseDTO>> getExercisesPaginated(
            @RequestParam(name = "difficulty", required = false) String difficulty,
            @RequestParam(name = "dataStructure", required = false) String dataStructure,
            @RequestParam(name = "company", required = false) String company,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {

        PagedResponseDTO<ExerciseDTO> exercises = exerciseService.findExercisesPaginated(
                difficulty, dataStructure, company, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(exercises);
    }
}
