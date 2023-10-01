package com.tofu.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LabelResponse {
    private String id;
    private String name;
    private String description;
}
