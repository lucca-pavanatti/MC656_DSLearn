package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.mappers.DataStructureMapper;
import com.mc656.dslearn.models.DSATopic;
import com.mc656.dslearn.repositories.DSARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DSATopicsService {

    private DataStructureMapper dataStructureMapper;

    private DSARepository dsaRepository;

    public DSATopicsService(DataStructureMapper dataStructureMapper, DSARepository dsaRepository) {
        this.dataStructureMapper = dataStructureMapper;
        this.dsaRepository = dsaRepository;
    }

    public DSADetailDTO findDetailsByName(String name) {
        if (name == null) {
            return null;
        }
        DSATopic topic = dsaRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado"));
        return dataStructureMapper.toDto(topic);
    }

    public PagedResponseDTO<DSADetailDTO> findAllTopicsPaginated(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        var sort = sortDirection.equalsIgnoreCase("desc") ?
                org.springframework.data.domain.Sort.by(sortBy).descending() :
                org.springframework.data.domain.Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DSATopic> topicsPage = dsaRepository.findAll(pageable);

        var dtos = topicsPage
                .map(dataStructureMapper::toDto)
                .getContent();

        return PagedResponseDTO.<DSADetailDTO>builder()
                .content(dtos)
                .page(topicsPage.getNumber())
                .size(topicsPage.getSize())
                .totalElements(topicsPage.getTotalElements())
                .totalPages(topicsPage.getTotalPages())
                .first(topicsPage.isFirst())
                .last(topicsPage.isLast())
                .build();
    }
}
