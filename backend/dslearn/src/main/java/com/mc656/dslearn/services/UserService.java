package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.dtos.UserMetricsDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserExercise;
import com.mc656.dslearn.models.UserTopic;
import com.mc656.dslearn.repositories.DSARepository;
import com.mc656.dslearn.repositories.ExerciseRepository;
import com.mc656.dslearn.repositories.UserExerciseRepository;
import com.mc656.dslearn.repositories.UserRepository;
import com.mc656.dslearn.repositories.UserTopicRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserExerciseRepository userExerciseRepository;
    private final UserTopicRepository userTopicRepository;
    private final ExerciseRepository exerciseRepository;
    private final DSARepository dsaRepository;

    public UserService(AuthService authService,
                       UserRepository userRepository,
                       UserExerciseRepository userExerciseRepository,
                       UserTopicRepository userTopicRepository,
                       ExerciseRepository exerciseRepository,
                       DSARepository dsaRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userExerciseRepository = userExerciseRepository;
        this.userTopicRepository = userTopicRepository;
        this.exerciseRepository = exerciseRepository;
        this.dsaRepository = dsaRepository;
    }

    public UserInfoDTO getUserInfoByToken(String idTokenString) throws GeneralSecurityException, IOException {
        User user = authService.verifyTokenAndGetUserEntity(idTokenString);
        if (user == null) return null;
        return buildUserInfoDTO(user);
    }

    public UserInfoDTO updateUserInfoFromToken(String idTokenString) throws GeneralSecurityException, IOException {
        User user = authService.verifyTokenAndGetUserEntity(idTokenString);
        if (user == null) return null;
        user = userRepository.save(user);
        return buildUserInfoDTO(user);
    }

    private UserInfoDTO buildUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserMetricsDTO getUserMetrics(Long userId) {
        // Verificar se o usuário existe
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Obter todos os tópicos e exercícios do sistema
        List<DSATopic> allTopics = dsaRepository.findAllTopics();
        List<Exercise> allExercises = exerciseRepository.findAllExercises().stream()
                .filter(ex -> {
                    if (ex.getRelatedTopics() == null || ex.getRelatedTopics().isEmpty()) {
                        return false;
                    }
                    String[] topics = ex.getRelatedTopics().split(",");
                    for (String topic : topics) {
                        String topicName = topic.trim();
                        if (allTopics.stream().anyMatch(t -> t.getName().equalsIgnoreCase(topicName))) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();

        // Obter progresso do usuário
        List<UserTopic> userTopics = userTopicRepository.findByUserId(userId);
        List<UserExercise> userExercises = userExerciseRepository.findByUserId(userId);

        // Calcular métricas de tópicos
        int totalTopics = allTopics.size();
        int completedTopics = countByStatus(userTopics, "COMPLETED");
        int inProgressTopics = countByStatus(userTopics, "IN_PROGRESS");
        int notStartedTopics = totalTopics - completedTopics - inProgressTopics;
        double topicsCompletionPercentage = totalTopics > 0 ? (completedTopics * 100.0 / totalTopics) : 0.0;

        // Calcular métricas de exercícios
        int totalExercises = allExercises.size();
        int completedExercises = countExercisesByStatus(userExercises, "COMPLETED");
        int inProgressExercises = countExercisesByStatus(userExercises, "IN_PROGRESS");
        int notStartedExercises = totalExercises - completedExercises - inProgressExercises;
        double exercisesCompletionPercentage = totalExercises > 0 ? (completedExercises * 100.0 / totalExercises) : 0.0;

        // Calcular métricas por dificuldade
        Map<String, UserMetricsDTO.ExercisesByDifficultyMetrics> exercisesByDifficulty = calculateExercisesByDifficulty(allExercises, userExercises);

        // Calcular métricas por tópico
        List<UserMetricsDTO.ExercisesByTopicMetrics> exercisesByTopic = calculateExercisesByTopic(allExercises, userExercises);

        // Calcular progresso geral (média ponderada: 40% tópicos + 60% exercícios)
        double overallProgress = (topicsCompletionPercentage * 0.4) + (exercisesCompletionPercentage * 0.6);

        return UserMetricsDTO.builder()
                .totalTopics(totalTopics)
                .completedTopics(completedTopics)
                .inProgressTopics(inProgressTopics)
                .notStartedTopics(notStartedTopics)
                .topicsCompletionPercentage(Math.round(topicsCompletionPercentage * 100.0) / 100.0)
                .totalExercises(totalExercises)
                .completedExercises(completedExercises)
                .inProgressExercises(inProgressExercises)
                .notStartedExercises(notStartedExercises)
                .exercisesCompletionPercentage(Math.round(exercisesCompletionPercentage * 100.0) / 100.0)
                .exercisesByDifficulty(exercisesByDifficulty)
                .exercisesByTopic(exercisesByTopic)
                .overallProgress(Math.round(overallProgress * 100.0) / 100.0)
                .build();
    }

    private int countByStatus(List<UserTopic> userTopics, String status) {
        return (int) userTopics.stream()
                .filter(ut -> status.equals(ut.getStatus()))
                .count();
    }

    private int countExercisesByStatus(List<UserExercise> userExercises, String status) {
        return (int) userExercises.stream()
                .filter(ue -> status.equals(ue.getStatus()))
                .count();
    }

    private Map<String, UserMetricsDTO.ExercisesByDifficultyMetrics> calculateExercisesByDifficulty(
            List<Exercise> allExercises, List<UserExercise> userExercises) {

        Map<String, UserMetricsDTO.ExercisesByDifficultyMetrics> result = new HashMap<>();

        // Criar um mapa de exerciseId -> status do usuário
        Map<Long, String> exerciseStatusMap = userExercises.stream()
                .collect(Collectors.toMap(
                        ue -> ue.getExercise().getId(),
                        UserExercise::getStatus,
                        (existing, replacement) -> existing
                ));

        // Agrupar exercícios por dificuldade
        Map<String, List<Exercise>> exercisesByDifficultyMap = allExercises.stream()
                .filter(ex -> ex.getDifficulty() != null)
                .collect(Collectors.groupingBy(ex -> ex.getDifficulty().toString()));

        // Calcular métricas para cada dificuldade
        for (Map.Entry<String, List<Exercise>> entry : exercisesByDifficultyMap.entrySet()) {
            String difficulty = entry.getKey();
            List<Exercise> exercises = entry.getValue();

            int total = exercises.size();
            int completed = 0;
            int inProgress = 0;
            int notStarted = 0;

            for (Exercise exercise : exercises) {
                String status = exerciseStatusMap.get(exercise.getId());
                if ("COMPLETED".equals(status)) {
                    completed++;
                } else if ("IN_PROGRESS".equals(status)) {
                    inProgress++;
                } else {
                    notStarted++;
                }
            }

            double completionPercentage = total > 0 ? (completed * 100.0 / total) : 0.0;

            result.put(difficulty, UserMetricsDTO.ExercisesByDifficultyMetrics.builder()
                    .total(total)
                    .completed(completed)
                    .inProgress(inProgress)
                    .notStarted(notStarted)
                    .completionPercentage(Math.round(completionPercentage * 100.0) / 100.0)
                    .build());
        }

        return result;
    }

    private List<UserMetricsDTO.ExercisesByTopicMetrics> calculateExercisesByTopic(
            List<Exercise> allExercises, List<UserExercise> userExercises) {

        // Criar um mapa de exerciseId -> status do usuário
        Map<Long, String> exerciseStatusMap = userExercises.stream()
                .collect(Collectors.toMap(
                        ue -> ue.getExercise().getId(),
                        UserExercise::getStatus,
                        (existing, replacement) -> existing
                ));

        // Obter todos os tópicos válidos da base de dados
        List<DSATopic> allTopics = dsaRepository.findAllTopics();
        Set<String> validTopicNames = allTopics.stream()
                .map(DSATopic::getName)
                .collect(Collectors.toSet());

        // Mapa para acumular estatísticas por tópico
        Map<String, TopicStats> topicStatsMap = new HashMap<>();

        // Processar cada exercício
        for (Exercise exercise : allExercises) {
            if (exercise.getRelatedTopics() != null && !exercise.getRelatedTopics().isEmpty()) {
                // Um exercício pode ter múltiplos tópicos separados por vírgula
                String[] topics = exercise.getRelatedTopics().split(",");

                for (String topic : topics) {
                    String topicName = topic.trim();
                    // Apenas processar tópicos que existem na base de dados
                    if (!topicName.isEmpty() && validTopicNames.contains(topicName)) {
                        TopicStats stats = topicStatsMap.computeIfAbsent(topicName, k -> new TopicStats());
                        stats.total++;

                        String status = exerciseStatusMap.get(exercise.getId());
                        if ("COMPLETED".equals(status)) {
                            stats.completed++;
                        } else if ("IN_PROGRESS".equals(status)) {
                            stats.inProgress++;
                        } else {
                            stats.notStarted++;
                        }
                    }
                }
            }
        }

        // Converter para lista de DTOs e ordenar por nome do tópico
        return topicStatsMap.entrySet().stream()
                .map(entry -> {
                    String topicName = entry.getKey();
                    TopicStats stats = entry.getValue();
                    double completionPercentage = stats.total > 0 ? (stats.completed * 100.0 / stats.total) : 0.0;

                    return UserMetricsDTO.ExercisesByTopicMetrics.builder()
                            .topicName(topicName)
                            .totalExercises(stats.total)
                            .completedExercises(stats.completed)
                            .inProgressExercises(stats.inProgress)
                            .notStartedExercises(stats.notStarted)
                            .completionPercentage(Math.round(completionPercentage * 100.0) / 100.0)
                            .build();
                })
                .sorted(Comparator.comparing(UserMetricsDTO.ExercisesByTopicMetrics::getTopicName))
                .collect(Collectors.toList());
    }

    // Classe auxiliar para acumular estatísticas
    private static class TopicStats {
        int total = 0;
        int completed = 0;
        int inProgress = 0;
        int notStarted = 0;
    }

}
