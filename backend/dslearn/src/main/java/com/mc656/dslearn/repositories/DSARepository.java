package com.mc656.dslearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.mc656.dslearn.models.DSATopic;

import java.util.Optional;

@Repository
public interface DSARepository extends PagingAndSortingRepository<DSATopic, Long> {
    Optional<DSATopic> findByName(String name);

    @Query("SELECT d FROM DSATopic d")
    List<DSATopic> findAllTopics();
}
