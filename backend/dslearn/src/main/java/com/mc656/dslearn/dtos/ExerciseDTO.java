package com.mc656.dslearn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String title;
    private String url;
    private String leetcodeUrl;
    private String difficulty;
    private List<String> companies;
}
