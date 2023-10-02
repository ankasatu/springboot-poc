package com.tofu.demo.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookLabelResponse {
    private LabelResponse label;
    private List<BookResponse> books;
}
