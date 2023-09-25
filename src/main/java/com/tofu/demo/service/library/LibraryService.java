package com.tofu.demo.service.library;


import com.tofu.demo.service.dto.BookDetailResponse;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;

import java.util.List;

public interface LibraryService {
    List<BookResponse> getBooks();
    BookDetailResponse getBookDetail(String isbn);
    String bookCreate(BookRequest request);
    void bookUpdate(String id, BookRequest request);
    void bookDelete(String id);

    void labelAssign(String bookId, List<String> nameList);
}
