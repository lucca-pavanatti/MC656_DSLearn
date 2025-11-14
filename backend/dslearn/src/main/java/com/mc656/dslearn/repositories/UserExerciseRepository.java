package com.mc656.dslearn.repositories;

import com.mc656.dslearn.models.UserExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserExerciseRepository extends JpaRepository<UserExercise, Long> {
    Optional<UserExercise> findByUserIdAndExerciseId(Long userId, Long exerciseId);
    List<UserExercise> findByUserId(Long id);

}
