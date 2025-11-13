package com.mc656.dslearn.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
}
