package com.mc656.dslearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mc656.dslearn.models.DSATopic;

@Repository
public interface DSARepository extends JpaRepository<DSATopic, Long> {
    DSATopic findByName(String name);
}
