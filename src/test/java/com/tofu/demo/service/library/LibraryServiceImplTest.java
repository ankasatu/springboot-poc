package com.tofu.demo.service.library;

import com.tofu.demo.Utils;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.repository.BookLabelRepository;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    BookRequest request;

    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);

        service = new LibraryServiceImpl(bookRepository, bookLabelRepository);

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

    @Test
    public void testBookCreate_Success() {
        when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());

        String result = service.bookCreate(request);

        assertTrue(Utils.matchUuidFormat.matches(result));
        assertNotNull(result);
        verify(bookRepository, times(1)).findByIsbn(any());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testBookCreate_IsbnAlreadyExists() {
        when(bookRepository.findByIsbn(any())).thenReturn(Optional.of(Book.builder().build()));

        assertThrowsExactly(ValidationException.class, () -> service.bookCreate(request));

        verify(bookRepository, times(1)).findByIsbn(any());
        verify(bookRepository, never()).save(any(Book.class));
    }


}
