package com.mc656.dslearn.mappers;

import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    List<ExerciseDTO> toDtoList(List<Exercise> exercises);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title") 
    @Mapping(target = "url", source = "url")
    @Mapping(target = "companies", source = "companies")
    @Mapping(target = "difficulty", expression = "java(mapDifficulty(exercise.getDifficulty()))")
    ExerciseDTO toDto(Exercise exercise);
    
    default String mapDifficulty(Difficulty difficulty) {
        return difficulty != null ? difficulty.name() : null;
    }
}
