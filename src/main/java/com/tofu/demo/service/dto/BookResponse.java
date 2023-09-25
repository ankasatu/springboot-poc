package com.tofu.demo.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int year;
}
