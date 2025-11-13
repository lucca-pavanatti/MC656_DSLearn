package com.mc656.dslearn.services;

// Em com.mc656.dslearn.services
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.ProgressTrackable; // Importe a interface
import com.mc656.dslearn.repositories.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractProgressService<
        ITEM,       // A entidade do item (ex: Exercise, DSATopic)
        ITEM_ID,    // O ID do item (ex: Long, String)
        PROGRESS extends ProgressTrackable, // A entidade de progresso (ex: UserExercise, UserTopic)
        PROGRESS_DTO // O DTO de progresso (ex: ExerciseProgressDTO, TopicProgressDTO)
        > {

    private final UserRepository userRepository;

    protected AbstractProgressService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected abstract ITEM findItem(ITEM_ID itemId);

    protected abstract PROGRESS findOrCreateProgress(User user, ITEM item);

    protected abstract void saveProgress(PROGRESS progress);

    protected abstract List<PROGRESS> findAllProgressByUserId(Long userId);

    protected abstract PROGRESS_DTO toDTO(PROGRESS progress);

    public void updateStatus(Long userId, ITEM_ID itemId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        ITEM item = findItem(itemId);

        PROGRESS progress = findOrCreateProgress(user, item);

        progress.setStatus(status);

        saveProgress(progress);
    }

    public List<PROGRESS_DTO> getProgress(Long userId) {
        return findAllProgressByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}