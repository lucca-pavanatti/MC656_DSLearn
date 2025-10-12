package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.services.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExercisesController {

    @Autowired
    private ExercisesService exerciseService;

    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getExercises(
            @RequestParam(name = "difficulty", required = false) String difficulty,
            @RequestParam(name = "dataStructure", required = false) String dataStructure,
            @RequestParam(name = "company", required = false) String company) {

        List<ExerciseDTO> exercises = exerciseService.findExercises(difficulty, dataStructure, company);
        return ResponseEntity.ok(exercises);
    }
}
