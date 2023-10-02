package com.tofu.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.model.Label;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LabelResponse {
    private String id;
    private String name;
    private String description;

    public static LabelResponse converter(BookLabel p) {
        return LabelResponse.builder()
                .id(p.getLabel().getId())
                .name(p.getLabel().getName())
                .description(p.getLabel().getDescription())
                .build();
    }
    public static LabelResponse converter(Label p) {
        return LabelResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .build();
    }
}
