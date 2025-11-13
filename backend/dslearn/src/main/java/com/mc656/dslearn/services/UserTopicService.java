package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.TopicProgressDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.models.UserTopic;
import com.mc656.dslearn.repositories.DSARepository;
import com.mc656.dslearn.repositories.UserRepository;
import com.mc656.dslearn.repositories.UserTopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTopicService extends AbstractProgressService<DSATopic, String, UserTopic, TopicProgressDTO> {

    private final UserTopicRepository userTopicRepository;
    private final DSARepository dsaRepository;

    public UserTopicService(UserRepository userRepository,
                            UserTopicRepository userTopicRepository,
                            DSARepository dsaRepository) {
        super(userRepository);
        this.userTopicRepository = userTopicRepository;
        this.dsaRepository = dsaRepository;
    }

    public List<TopicProgressDTO> getTopicsProgress(Long userId) {
        return super.getProgress(userId);
    }

    @Override
    protected DSATopic findItem(String topicName) {
        return dsaRepository.findByName(topicName)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado"));
    }

    @Override
    protected UserTopic findOrCreateProgress(User user, DSATopic topic) {
        return userTopicRepository
                .findByUserIdAndTopicName(user.getId(), topic.getName())
                .orElseGet(() -> new UserTopic(user, topic));
    }

    @Override
    protected void saveProgress(UserTopic progress) {
        userTopicRepository.save(progress);
    }

    @Override
    protected List<UserTopic> findAllProgressByUserId(Long userId) {
        return userTopicRepository.findByUserId(userId);
    }

    @Override
    protected TopicProgressDTO toDTO(UserTopic userTopic) {
        return new TopicProgressDTO(
                userTopic.getTopic().getName(),
                userTopic.getStatus()
        );
    }
}