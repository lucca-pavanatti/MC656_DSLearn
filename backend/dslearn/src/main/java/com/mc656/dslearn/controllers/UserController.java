package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.ExerciseProgressDTO;
import com.mc656.dslearn.dtos.ExerciseStatusRequestDTO;
import com.mc656.dslearn.dtos.TopicProgressDTO;
import com.mc656.dslearn.dtos.TopicStatusRequestDTO;
import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.dtos.UserMetricsDTO;
import com.mc656.dslearn.services.UserExerciseService;
import com.mc656.dslearn.services.UserService;
import com.mc656.dslearn.services.UserTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "User management and progress tracking APIs")
public class UserController {

    private final UserService userService;
    private final UserExerciseService userExerciseService;
    private final UserTopicService userTopicService;

    public UserController(UserService userService, UserExerciseService userExerciseService, UserTopicService userTopicService) {
        this.userService = userService;
        this.userExerciseService = userExerciseService;
        this.userTopicService = userTopicService;
    }

    @Operation(summary = "Get user information", description = "Retrieve user information by Google ID token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid token"),
        @ApiResponse(responseCode = "400", description = "Token verification failed")
    })
    @SecurityRequirement(name = "token-header")
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(
            @Parameter(description = "Google ID token", required = true)
            @RequestHeader("Token") String idToken) {
        try {
            UserInfoDTO userInfo = userService.getUserInfoByToken(idToken);
            if (userInfo == null) return ResponseEntity.status(401).body("Invalid token");
            return ResponseEntity.ok(userInfo);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(400).body("Token verification failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Update exercise status", description = "Update the status of an exercise for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exercise status updated successfully")
    })
    @PutMapping("/{userId}/exercises/status")
    public ResponseEntity<?> updateExerciseStatus(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId,
            @RequestBody ExerciseStatusRequestDTO req
    ) {
        userExerciseService.updateStatus(userId, req.getExerciseId(), req.getStatus());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update topic status", description = "Update the status of a topic for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Topic status updated successfully")
    })
    @PutMapping("/{userId}/topics/status")
    public ResponseEntity<?> updateTopicStatus(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId,
            @RequestBody TopicStatusRequestDTO req
    ) {
        userTopicService.updateStatus(userId, req.getTopicName(), req.getStatus());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get exercises progress", description = "Retrieve progress for all exercises for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exercise progress retrieved successfully")
    })
    @GetMapping("/{userId}/exercises/progress")
    public ResponseEntity<List<ExerciseProgressDTO>> getExercisesProgress(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId
    ) {

        var progresso = userExerciseService.getExercisesProgress(userId);
        return ResponseEntity.ok(progresso);
    }

    @Operation(summary = "Get topics progress", description = "Retrieve progress for all topics for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Topic progress retrieved successfully")
    })
    @GetMapping("/{userId}/topics/progress")
    public ResponseEntity<List<TopicProgressDTO>> getTopicsProgress(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId
    ) {

        var progresso = userTopicService.getTopicsProgress(userId);
        return ResponseEntity.ok(progresso);
    }

    @Operation(summary = "Get user metrics", description = "Retrieve various metrics for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User metrics retrieved successfully")
    })
    @GetMapping("/{id}/metrics")
    public ResponseEntity<UserMetricsDTO> getUserMetrics(
            @Parameter(description = "User ID", required = true)
            @PathVariable("id") Long userId
    ) {
        UserMetricsDTO metrics = userService.getUserMetrics(userId);
        return ResponseEntity.ok(metrics);
    }

}
