package com.mc656.dslearn.repositories;

import com.mc656.dslearn.models.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {
    Optional<UserTopic> findByUserIdAndTopicName(Long userId, String name);

    List<UserTopic> findByUserId(Long userId);

}
