package com.tofu.demo.service.book;

import com.tofu.demo.exception.DataNotFoundException;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookDetailResponse;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    BookServiceImpl bookService;


    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        bookService = new BookServiceImpl(bookRepository);
    }

    @Nested
    class GetBookbyIsbn {
        @Test
        void shouldSuccess() {
            Book existingBook = Book.builder()
                    .id("Hello World")
                    .labels(List.of(
                            BookLabel.builder().label(Label.builder().build()).build()
                    ))
                    .build();
            when(bookRepository.findByIsbn(any())).thenReturn(Optional.of(existingBook));

            String isbn = "1234567890";
            BookDetailResponse response = bookService.getBookByIsbn(isbn);

            verify(bookRepository, times(1)).findByIsbn(isbn);
            assertNotNull(response);
            assertEquals(existingBook.getLabels().size(), response.getLabels().size());
        }

        @Test
        void shouldThrowIfBookNotFound() {
            when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());

            String isbn = "1234567890";
            assertThrows(DataNotFoundException.class, () -> bookService.getBookByIsbn(isbn));
            verify(bookRepository, times(1)).findByIsbn(isbn);
        }
    }

    @Nested
    class GetList {
        @Test
        void testGetList() {
            List<Book> books = List.of(
                    Book.builder().build(),
                    Book.builder().build()
            );

            when(bookRepository.findAll()).thenReturn(books);

            List<BookResponse> responseList = bookService.getList();

            verify(bookRepository, times(1)).findAll();
            assertEquals(books.size(), responseList.size());
        }
    }

    @Nested
    class Create {

        @Test
        void shouldSuccess() {
            String id = "book-id";
            when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());
            when(bookRepository.save(any())).thenReturn(Book.builder().id(id).build());

            BookRequest request = BookRequest.builder().build();
            var result = bookService.create(request);

            assertNotNull(result);
            assertEquals(id, result);
            verify(bookRepository, atLeastOnce()).save(any());
        }

        @Test
        void shouldThrowIfIsbnIsExist() {
            Book existingBook = Book.builder().id("Hello World").build();

            when(bookRepository.findByIsbn(any())).thenReturn(Optional.of(existingBook));

            BookRequest request = BookRequest.builder().build();
            assertThrows(ValidationException.class, () -> bookService.create(request));

            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    class Edit {
        @Test
        void shouldSuccess() {
            Book existingBook = Book.builder().id("Hello World").build();
            when(bookRepository.findById(any())).thenReturn(Optional.of(existingBook));

            String requestBookId = "12345";
            BookRequest request = BookRequest.builder().build();
            bookService.edit(requestBookId, request);

            verify(bookRepository, atLeastOnce()).save(any());
        }

        @Test
        void shouldThrowIfIdNotFound() {
            when(bookRepository.findById(any())).thenReturn(Optional.empty());

            String requestBookId = "12345";
            BookRequest request = BookRequest.builder().build();
            assertThrows(DataNotFoundException.class, () -> bookService.edit(requestBookId, request));

            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    class Delete {
        @Test
        void testDeleteBook_Success() {
            Book existingBook = Book.builder().build();
            when(bookRepository.findById(any())).thenReturn(Optional.of(existingBook));

            String bookId = "123";
            bookService.delete(bookId);

            verify(bookRepository, times(1)).delete(any());
        }

        @Test
        void testDeleteBook_BookNotFound() {
            when(bookRepository.findById(any())).thenReturn(Optional.empty());

            String bookId = "123";
            assertThrows(DataNotFoundException.class, () -> bookService.delete(bookId));

            verify(bookRepository, never()).delete(any(Book.class));
        }
    }
}
