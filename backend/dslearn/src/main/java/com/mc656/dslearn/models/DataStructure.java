package com.mc656.dslearn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataStructure {
    private String name;
    private String theory;
    private List<Exercise> exercises;
}