package com.tofu.demo.service.label;

import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.dto.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private BookRepository bookRepository;

    @Override
    public List<BookResponse> getBooks() {
        return bookRepository.findAll().stream().map(p-> BookResponse.builder()
                .id(p.getId())
                .author(p.getAuthor())
                .isbn(p.getIsbn())
                .title(p.getTitle())
                .year(p.getPublicationYear())
                .build()
        ).collect(Collectors.toList());
    }

    public void bookCreate(BookRequest request)
    {
        var book = bookRepository.findByIsbn(request.getIsbn());
        if (book.isPresent()) throw new ValidationException("isbn sudah ada");

        Book tmp = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .publicationYear(request.getYear())
                .build();

        bookRepository.save(tmp);
    }

    public void bookUpdate(String id, BookRequest request)
    {
        var book = bookRepository.findById(id).orElseThrow(()-> new ValidationException("whops"));

        book.setIsbn(request.getIsbn());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getYear());
        book.setTitle(request.getTitle());

        bookRepository.save(book);
    }

    public void bookDelete(String id)
    {
        var book = bookRepository.findById(id).orElseThrow(()-> new ValidationException("whops"));
        bookRepository.delete(book);
    }


















}
