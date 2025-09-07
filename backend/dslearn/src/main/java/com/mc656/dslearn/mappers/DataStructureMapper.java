package com.mc656.dslearn.mappers;

import com.mc656.dslearn.dtos.DataStructureDetailDTO;
import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.DataStructure;
import com.mc656.dslearn.models.Exercise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataStructureMapper {
    DataStructureDetailDTO toDto(DataStructure dataStructure);

    ExerciseDTO toDto(Exercise exercise);
}   