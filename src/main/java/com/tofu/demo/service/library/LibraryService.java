package com.tofu.demo.service.library;

import com.tofu.demo.service.dto.BookLabelResponse;

import java.util.List;

public interface LibraryService {
    void setBookLabel(String bookId, List<String> labelIds);
    BookLabelResponse getBooksByLabel(String labelId);

    void periodic1();
}
