package com.mc656.dslearn.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserInfoRequestDTO {
    private String idToken;
    private List<Long> startedTopics;
    private List<Long> completedTopics;
    private List<Long> startedExercises;
    private List<Long> completedExercises;
}
