package com.tofu.demo.service.library;

import com.tofu.demo.exception.DataNotFoundException;
import com.tofu.demo.exception.ValidationException;
import com.tofu.demo.model.Book;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.BookLabelRepository;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.*;
import com.tofu.demo.service.label.LabelService;
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

    private LabelService labelService;

    @Override
    public void setBookLabel(String bookId, List<String> labelIds)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new DataNotFoundException("id tidak ditemukan"));

        List<Label> labels = labelService.getLabelByIds(labelIds);

        List<BookLabel> bookLabels = labels.stream().map(label->BookLabel.builder()
                    .id(UUID.randomUUID().toString())
                    .book(book)
                    .label(label)
                    .build())
                .toList();

        bookLabelRepository.saveAll(bookLabels);
    }

    @Override
    public BookLabelResponse getBooksByLabel(String labelId) {
        List<Label> labels = labelService.getLabelByIds(List.of(labelId));

        if (labels.isEmpty()) {
            throw new DataNotFoundException("tidak ada label id");
        }

        Label label = labels.stream().findFirst().get();

        List<BookResponse> booksResult = label.getBookLabels().stream().map(p->BookResponse.builder()
                .id(p.getBook().getId())
                .isbn(p.getBook().getIsbn())
                .title(p.getBook().getTitle())
                .author(p.getBook().getAuthor())
                .publisher(p.getBook().getPublisher())
                .year(p.getBook().getYear())
                .build()).toList();

        return BookLabelResponse.builder()
                .label(LabelResponse.converter(label))
                .books(booksResult)
                .build();
    }

    @Override
    public void periodic1() {
        log.info("Periodic task: {}", LocalDateTime.now());
    }
}
