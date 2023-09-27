package com.tofu.demo.service.library;

import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.repository.BookLabelRepository;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookDetailResponse;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import com.tofu.demo.service.dto.LabelResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private BookRepository bookRepository;
    private BookLabelRepository bookLabelRepository;

    @Override
    public List<BookResponse> getBooks() {
        return bookRepository.findAll().stream().map(p-> BookResponse.builder()
                .id(p.getId())
                .author(p.getAuthor())
                .isbn(p.getIsbn())
                .title(p.getTitle())
                .publisher(p.getPublisher())
                .year(p.getPublicationYear())
                .build()
        ).toList();
    }


    public BookDetailResponse getBookDetail(String isbn) {
        var book = bookRepository.findByIsbn(isbn).orElseThrow(()-> new ValidationException("id tidak ditemukan"));
        var labels = book.getLabels().stream().map(p-> LabelResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .build()).toList();

        return BookDetailResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .year(book.getPublicationYear())
                .labels(labels)
                .build();
    }

    @Override
    public String bookCreate(BookRequest request)
    {
        var book = bookRepository.findByIsbn(request.getIsbn());
        if (book.isPresent()) throw new ValidationException("isbn sudah ada");

        Book tmp = Book.builder()
                .id(UUID.randomUUID().toString())
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .publicationYear(request.getYear())
                .build();

        var result = bookRepository.save(tmp);
        return result.getId();
    }

    @Override
    public void bookUpdate(String id, BookRequest request)
    {
        var book = bookRepository.findById(id).orElseThrow(()-> new ValidationException("id tidak ditemukan"));

        book.setIsbn(request.getIsbn());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getYear());
        book.setTitle(request.getTitle());

        bookRepository.save(book);
    }

    @Override
    public void bookDelete(String id)
    {
        var book = bookRepository.findById(id).orElseThrow(()-> new ValidationException("book id tidak ditemukan"));
        bookRepository.delete(book);
    }

    @Override
    public void labelAssign(String bookId, List<String> nameList)
    {
        var book = bookRepository.findById(bookId).orElseThrow(()-> new ValidationException("book id tidak ditemukan"));

        var labels = nameList.stream().map(p->BookLabel.builder()
                .id(UUID.randomUUID().toString())
                        .book(book)
                        .name(p)
                .build()).toList();
        bookLabelRepository.saveAll(labels);
    }

    @Override
    public void periodic1() {
        log.info("Periodic task: {}", LocalDateTime.now());
    }
}
