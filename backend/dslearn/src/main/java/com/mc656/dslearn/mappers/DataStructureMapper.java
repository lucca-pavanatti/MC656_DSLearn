package com.mc656.dslearn.mappers;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.dtos.ExerciseDTO;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.Exercise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataStructureMapper {
    DSADetailDTO toDto(DSATopic dataStructure);

    ExerciseDTO toDto(Exercise exercise);
}   