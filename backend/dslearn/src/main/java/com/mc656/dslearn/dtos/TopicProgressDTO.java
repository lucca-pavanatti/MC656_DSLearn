package com.mc656.dslearn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TopicProgressDTO {
    private String name;
    private String status;
}
