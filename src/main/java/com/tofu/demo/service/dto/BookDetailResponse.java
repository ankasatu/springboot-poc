package com.tofu.demo.service.dto;

import com.tofu.demo.model.BookLabel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDetailResponse {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private List<LabelResponse> labels;
}
