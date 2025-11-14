package com.mc656.dslearn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseStatusRequestDTO {
    @JsonProperty("exercise_id")
    private Long exerciseId;
    private String status;

}
