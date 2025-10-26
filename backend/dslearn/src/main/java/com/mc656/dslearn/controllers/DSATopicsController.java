package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.DSADetailDTO;
import com.mc656.dslearn.services.DSATopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
public class DSATopicsController {

    @Autowired
    private DSATopicsService dataStructureService;

    @GetMapping("/{name}")
    public ResponseEntity<DSADetailDTO> getTopicByName(@PathVariable String name) {
        DSADetailDTO dto = dataStructureService.findDetailsByName(name);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}