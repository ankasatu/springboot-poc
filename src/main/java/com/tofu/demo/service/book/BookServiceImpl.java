package com.tofu.demo.service.book;

import com.tofu.demo.exception.DataNotFoundException;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookDetailResponse;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public BookDetailResponse getBookByIsbn(String isbn) {
        var book = bookRepository.findByIsbn(isbn)
                .orElseThrow(()-> new DataNotFoundException("id tidak ditemukan"));

        return BookDetailResponse.converter(book);
    }

    @Override
    public List<BookResponse> getList() {
        return bookRepository.findAll().stream().map(BookResponse::converter).toList();
    }

    @Override
    public String create(BookRequest request) {
        var book = bookRepository.findByIsbn(request.getIsbn());

        if (book.isPresent()) throw new ValidationException("isbn sudah ada");

        Book tmp = Book.builder()
                .id(UUID.randomUUID().toString())
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .year(request.getYear())
                .build();

        var result = bookRepository.save(tmp);
        return result.getId();
    }

    @Override
    public void edit(String id, BookRequest request) {
        var book = bookRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("id tidak ditemukan"));

        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setYear(request.getYear());
        book.setTitle(request.getTitle());

        bookRepository.save(book);

    }

    @Override
    public void delete(String id) {
        var book = bookRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("id tidak ditemukan"));

        bookRepository.delete(book);
    }
}
