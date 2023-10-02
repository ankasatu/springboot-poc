package com.tofu.demo.service.book;

import com.tofu.demo.service.dto.*;

import java.util.List;

public interface BookService {
    BookDetailResponse getBookByIsbn(String isbn);
    List<BookResponse> getList();

    String create(BookRequest request);
    void edit(String id, BookRequest request);
    void delete(String id);
}
