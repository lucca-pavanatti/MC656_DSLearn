package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.services.ExercisesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
@Tag(name = "Exercises", description = "Exercise management APIs")
public class ExercisesController {

    private ExercisesService exerciseService;

    public ExercisesController(ExercisesService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Operation(summary = "Get exercises paginated", description = "Retrieve exercises with pagination and optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exercises retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PagedResponseDTO<ExerciseDTO>> getExercisesPaginated(
            @Parameter(description = "Filter by difficulty level")
            @RequestParam(name = "difficulty", required = false) String difficulty,
            @Parameter(description = "Filter by data structure")
            @RequestParam(name = "dataStructure", required = false) String dataStructure,
            @Parameter(description = "Filter by company")
            @RequestParam(name = "company", required = false) String company,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(name = "size", defaultValue = "10") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {

        PagedResponseDTO<ExerciseDTO> exercises = exerciseService.findExercisesPaginated(
                difficulty, dataStructure, company, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(exercises);
    }
}
