package com.tofu.demo.service.label;


import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;

import java.util.List;

public interface LabelService {
    List<BookResponse> getBooks();
    void bookCreate(BookRequest request);
    void bookUpdate(String id, BookRequest request);
    void bookDelete(String id);
}
