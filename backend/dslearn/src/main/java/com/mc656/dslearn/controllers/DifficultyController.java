package com.mc656.dslearn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // GET /api/exercises/EASY
    @GetMapping("/{difficulty}")
    public ResponseEntity<List<Exercise>> getExercisesByDifficulty(
            @PathVariable Difficulty difficulty
    ) {
        List<Exercise> exercises = exerciseService.findByDifficulty(difficulty);

        if (exercises.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(exercises);
        }
    }
}
