package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.ExerciseProgressDTO;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserExercise;
import com.mc656.dslearn.repositories.ExerciseRepository;
import com.mc656.dslearn.repositories.UserExerciseRepository;
import com.mc656.dslearn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserExerciseService extends AbstractProgressService<Exercise, Long, UserExercise, ExerciseProgressDTO> {

    private final UserExerciseRepository userExerciseRepository;
    private final ExerciseRepository exerciseRepository;

    public UserExerciseService(UserRepository userRepository,
                               UserExerciseRepository userExerciseRepository,
                               ExerciseRepository exerciseRepository) {
        super(userRepository);
        this.userExerciseRepository = userExerciseRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<ExerciseProgressDTO> getExercisesProgress(Long userId) {
        return super.getProgress(userId);
    }

    @Override
    protected Exercise findItem(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercício não encontrado"));
    }

    @Override
    protected UserExercise findOrCreateProgress(User user, Exercise exercicio) {
        return userExerciseRepository
                .findByUserIdAndExerciseId(user.getId(), exercicio.getId())
                .orElseGet(() -> new UserExercise(user, exercicio));
    }

    @Override
    protected void saveProgress(UserExercise progress) {
        userExerciseRepository.save(progress);
    }

    @Override
    protected List<UserExercise> findAllProgressByUserId(Long userId) {
        return userExerciseRepository.findByUserId(userId);
    }

    @Override
    protected ExerciseProgressDTO toDTO(UserExercise userExercise) {
        return new ExerciseProgressDTO(
                userExercise.getExercise().getId(),
                userExercise.getExercise().getTitle(),
                userExercise.getStatus()
        );
    }
}
