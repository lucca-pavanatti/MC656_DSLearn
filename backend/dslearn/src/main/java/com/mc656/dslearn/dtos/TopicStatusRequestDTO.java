package com.mc656.dslearn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicStatusRequestDTO {
    @JsonProperty("topic_name")
    private String topicName;
    private String status;
}
