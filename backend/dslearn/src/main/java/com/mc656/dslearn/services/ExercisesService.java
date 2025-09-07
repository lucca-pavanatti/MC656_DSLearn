package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.mappers.ExerciseMapper;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExercisesService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    private final List<Exercise> allExercises = List.of(
            new Exercise(1L, "Two Sum", "https://leetcode.com/problems/two-sum/", Difficulty.Easy, List.of("Array", "Hash Table"), List.of("Google", "Facebook", "Amazon")),
            new Exercise(11L, "Container With Most Water", "https://leetcode.com/problems/container-with-most-water/", Difficulty.Medium, List.of("Array", "Two Pointers", "Greedy"), List.of("Google", "Bloomberg")),
            new Exercise(15L, "3Sum", "https://leetcode.com/problems/3sum/", Difficulty.Medium, List.of("Array", "Two Pointers", "Sorting"), List.of("Facebook", "Microsoft", "Amazon")),
            new Exercise(20L, "Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/", Difficulty.Easy, List.of("Stack", "String"), List.of("Google", "Amazon", "Facebook")),
            new Exercise(155L, "Min Stack", "https://leetcode.com/problems/min-stack/", Difficulty.Easy, List.of("Stack", "Design"), List.of("Amazon", "Bloomberg", "Uber")),
            new Exercise(150L, "Evaluate Reverse Polish Notation", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", Difficulty.Medium, List.of("Stack", "Array"), List.of("LinkedIn", "Google"))
    );

    public List<ExerciseDTO> findExercises(Difficulty difficulty, String dataStructure) {
        Stream<Exercise> exerciseStream = allExercises.stream();

        if (dataStructure != null && !dataStructure.isBlank()) {
            exerciseStream = exerciseStream.filter(exercise ->
                    exercise.getRelatedTopics().stream()
                            .anyMatch(topic -> topic.equalsIgnoreCase(dataStructure))
            );
        }

        if (difficulty != null) {
            exerciseStream = exerciseStream.filter(exercise ->
                    exercise.getDifficulty() == difficulty
            );
        }

        List<Exercise> filteredExercises = exerciseStream.collect(Collectors.toList());
        return exerciseMapper.toDtoList(filteredExercises);
    }
}
