package com.mc656.dslearn.models;

import com.mc656.dslearn.models.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    private Long id;
    private String title;
    private String url;
    private Difficulty difficulty;
    private List<String> companies;
}
