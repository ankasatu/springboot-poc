package com.tofu.demo.service.dto;

import com.tofu.demo.model.Book;
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

    public static BookDetailResponse converter(Book book) {
        return BookDetailResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .labels(book.getLabels().stream().map(LabelResponse::converter).toList())
                .build();
    }
}
