package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.DataStructureDetailDTO;
import com.mc656.dslearn.mappers.DataStructureMapper;
import com.mc656.dslearn.models.DataStructure;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataStructuresService {
    @Autowired
    private DataStructureMapper dataStructureMapper;

    public DataStructureDetailDTO findDetailsByName(String name) {
        if (name == null) {
            return null;
        }
        DataStructure model = switch (name) {
            case "Array" -> {
                List<Exercise> exercises = List.of(
                        new Exercise(1L, "Two Sum", "https://leetcode.com/problems/two-sum/", Difficulty.Easy, List.of("Array", "Hash Table"), List.of("Google", "Facebook", "Amazon")),
                        new Exercise(11L, "Container With Most Water", "https://leetcode.com/problems/container-with-most-water/", Difficulty.Medium, List.of("Array", "Two Pointers", "Greedy"), List.of("Google", "Bloomberg")),
                        new Exercise(15L, "3Sum", "https://leetcode.com/problems/3sum/", Difficulty.Medium, List.of("Array", "Two Pointers", "Sorting"), List.of("Facebook", "Microsoft", "Amazon"))
                        );
                yield new DataStructure("Array", "Um array é uma coleção de itens armazenados em locais de memória contíguos.", exercises);
            }
            case "Stack" -> {
                List<Exercise> exercises = List.of(
                        new Exercise(20L, "Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/", Difficulty.Easy, List.of("Stack", "String"), List.of("Google", "Amazon", "Facebook")),
                        new Exercise(155L, "Min Stack", "https://leetcode.com/problems/min-stack/", Difficulty.Easy, List.of("Stack", "Design"), List.of("Amazon", "Bloomberg", "Uber")),
                        new Exercise(150L, "Evaluate Reverse Polish Notation", "https://leetcode.com/problems/evaluate-reverse-polish-notation/", Difficulty.Medium, List.of("Stack", "Array"), List.of("LinkedIn", "Google"))
                );
                yield new DataStructure("Stack", "Uma pilha é uma estrutura de dados LIFO (Last-In, First-Out).", exercises);
            }
            default -> null;
        };

        return dataStructureMapper.toDto(model);
    }
}
