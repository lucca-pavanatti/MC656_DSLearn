package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.dtos.PagedResponseDTO;
import com.mc656.dslearn.services.DSATopicsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
@Tag(name = "DSA Topics", description = "Data Structures and Algorithms topics management APIs")
public class DSATopicsController {

    private DSATopicsService dataStructureService;

    public DSATopicsController(DSATopicsService dataStructureService) {
        this.dataStructureService = dataStructureService;
    }

    @Operation(summary = "Get topic by name", description = "Retrieve detailed information about a specific DSA topic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Topic found"),
        @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/{name}")
    public ResponseEntity<DSADetailDTO> getTopicByName(
            @Parameter(description = "Topic name", required = true)
            @PathVariable String name) {
        DSADetailDTO dto = dataStructureService.findDetailsByName(name);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all topics paginated", description = "Retrieve all DSA topics with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Topics retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PagedResponseDTO<DSADetailDTO>> getAllTopicsPaginated(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(name = "size", defaultValue = "10") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {

        PagedResponseDTO<DSADetailDTO> topics = dataStructureService.findAllTopicsPaginated(
                page, size, sortBy, sortDirection);
        return ResponseEntity.ok(topics);
    }
}