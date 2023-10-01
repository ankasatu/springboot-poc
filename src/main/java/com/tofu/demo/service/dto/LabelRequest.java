package com.tofu.demo.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabelRequest {
    private String name;
    private String description;
}