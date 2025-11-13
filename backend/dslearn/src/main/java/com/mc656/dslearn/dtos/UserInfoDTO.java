package com.mc656.dslearn.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
    private List<Long> startedTopics;
    private List<Long> completedTopics;
    private List<Long> startedExercises;
    private List<Long> completedExercises;
}
