package com.mc656.dslearn.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DSADetailDTO {
    private String name;
    private String contentMarkdown;
}
