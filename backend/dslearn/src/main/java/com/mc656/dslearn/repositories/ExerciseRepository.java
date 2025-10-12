package com.mc656.dslearn.repositories;

import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e\n" +
            "WHERE (:difficulty IS NULL OR e.difficulty = :difficulty)\n" +
            "AND (:dataStructure IS NULL OR :dataStructure = '' OR LOWER(e.relatedTopics) LIKE CONCAT('%', LOWER(:dataStructure), '%'))\n" +
            "AND (:company IS NULL OR :company = '' OR LOWER(e.companies) LIKE CONCAT('%', LOWER(:company), '%'))")
    List<Exercise> findByFilters(@Param("difficulty") Difficulty difficulty,
                                 @Param("dataStructure") String dataStructure,
                                 @Param("company") String company);
}
