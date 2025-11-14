package com.mc656.dslearn.repositories;

import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e\n" +
            "WHERE (:difficulty IS NULL OR e.difficulty = :difficulty)\n" +
            "AND (:dataStructure IS NULL OR :dataStructure = '' OR LOWER(e.relatedTopics) LIKE CONCAT('%', LOWER(:dataStructure), '%'))\n" +
            "AND (:company IS NULL OR :company = '' OR LOWER(e.companies) LIKE CONCAT('%', LOWER(:company), '%'))")
    Page<Exercise> findByFiltersPageable(@Param("difficulty") Difficulty difficulty,
                                        @Param("dataStructure") String dataStructure,
                                        @Param("company") String company,
                                        Pageable pageable);
    Optional<Exercise> findById(Long id);
}
