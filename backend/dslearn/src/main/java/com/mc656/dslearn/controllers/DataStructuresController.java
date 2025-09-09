package com.mc656.dslearn.controllers;

import com.mc656.dslearn.dtos.DataStructureDetailDTO;
import com.mc656.dslearn.services.DataStructuresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data-structures")
public class DataStructuresController {

    @Autowired
    private DataStructuresService dataStructureService;

    @GetMapping("/{name}")
    public ResponseEntity<DataStructureDetailDTO> getStructureById(@PathVariable String name) {
        DataStructureDetailDTO dto = dataStructureService.findDetailsByName(name);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}