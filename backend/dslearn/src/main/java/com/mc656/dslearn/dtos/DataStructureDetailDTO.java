package com.mc656.dslearn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataStructureDetailDTO {
    private String name;
    private String theory;
    private List<ExerciseDTO> exercises;
}
