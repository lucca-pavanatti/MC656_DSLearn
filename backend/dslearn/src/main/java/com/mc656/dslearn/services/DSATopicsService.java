package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.mappers.DataStructureMapper;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.models.Exercise;
import com.mc656.dslearn.models.enums.Difficulty;
import com.mc656.dslearn.repositories.DSARepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DSATopicsService {
    @Autowired
    private DataStructureMapper dataStructureMapper;

    @Autowired
    private DSARepository dsaRepository;

    public DSADetailDTO findDetailsByName(String name) {
        if (name == null) {
            return null;
        }
        DSATopic model = dsaRepository.findByName(name);
        Objects.requireNonNull(model, "DSA Topic not found: " + name);
        return dataStructureMapper.toDto(model);
    }
}
