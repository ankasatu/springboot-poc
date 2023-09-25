package com.tofu.demo.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabelResponse {
    private String id;
    private String name;
}
