package com.tofu.demo.service.library;

import com.tofu.demo.Utils;
import com.tofu.demo.exception.DataNotFoundException;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.BookLabelRepository;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.label.LabelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookLabelRepository bookLabelRepository;
    @Mock
    private LibraryService service;
    @Mock
    private LabelService labelService;

    BookRequest request;

    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);

        service = new LibraryServiceImpl(bookRepository, bookLabelRepository, labelService);

        Book savedBook = Book.builder()
                .id(UUID.randomUUID().toString())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        request = BookRequest.builder()
                .isbn("1234567890")
                .title("Test Book")
                .author("Test Author")
                .publisher("Test Publisher")
                .year(2023)
                .build();
    }

    @Nested
    class SetBooklabel {
        @Test
        void shouldSuccess() {
            Book existingBook = Book.builder().build();
            List<Label> labels = List.of(Label.builder().build(), Label.builder().build());

            when(bookRepository.findById(any())).thenReturn(Optional.of(existingBook));
            when(labelService.getLabelByIds(any())).thenReturn(labels);

            String bookId = "123";
            List<String> labelIds = List.of("labelId1", "labelId2");
            service.setBookLabel(bookId, labelIds);

            verify(bookRepository, times(1)).findById(bookId);
            verify(labelService, times(1)).getLabelByIds(labelIds);
            verify(bookLabelRepository, times(1)).saveAll(any());
        }

        @Test
        void shouldThrowIfBookNotFound() {
            String bookId = "123";
            List<String> labelIds = List.of("labelId1", "labelId2");

            when(bookRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(DataNotFoundException.class, () -> service.setBookLabel(bookId, labelIds));

            verify(labelService, never()).getLabelByIds(anyList());
            verify(bookLabelRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    class GetBooksByLabelId {
        Label label;
        @BeforeEach
        void before() {
            label = Label.builder().build();
            label.setBookLabels(List.of(BookLabel.builder().book(Book.builder().build()).build()));
        }

        @Test
        void shouldSuccess() {
            when(labelService.getLabelByIds(any())).thenReturn(List.of(label));

            var result = service.getBooksByLabel("12345");

            assertNotNull(result);
        }

        @Test
        void shouldThrow() {
            when(labelService.getLabelByIds(any())).thenReturn(List.of());

            assertThrowsExactly(DataNotFoundException.class, () -> service.getBooksByLabel("1234"));
        }
    }
}
