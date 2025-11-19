package com.mc656.dslearn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMetricsDTO {
    
    // Métricas gerais de tópicos
    private Integer totalTopics;
    private Integer completedTopics;
    private Integer inProgressTopics;
    private Integer notStartedTopics;
    private Double topicsCompletionPercentage;
    
    // Métricas gerais de exercícios
    private Integer totalExercises;
    private Integer completedExercises;
    private Integer inProgressExercises;
    private Integer notStartedExercises;
    private Double exercisesCompletionPercentage;
    
    // Métricas por dificuldade
    private Map<String, ExercisesByDifficultyMetrics> exercisesByDifficulty;
    
    // Métricas por tópico (quantidade de exercícios finalizados por tópico)
    private List<ExercisesByTopicMetrics> exercisesByTopic;
    
    // Progresso geral do usuário (0-100)
    private Double overallProgress;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExercisesByDifficultyMetrics {
        private Integer total;
        private Integer completed;
        private Integer inProgress;
        private Integer notStarted;
        private Double completionPercentage;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExercisesByTopicMetrics {
        private String topicName;
        private Integer totalExercises;
        private Integer completedExercises;
        private Integer inProgressExercises;
        private Integer notStartedExercises;
        private Double completionPercentage;
    }
}
