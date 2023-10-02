package com.tofu.demo.service.dto;

import com.tofu.demo.model.Book;
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

    public static BookResponse converter(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .build();
    }
}
