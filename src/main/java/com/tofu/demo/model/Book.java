package com.tofu.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book", indexes = {
        @Index(name = "isbn_ix", columnList = "isbn")
})
public class Book {
    @Id
    @GenericGenerator(name = "UUID")
    private String id;

    @Column(name = "isbn", nullable = false, length = 32)
    private String isbn;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "author", nullable = false, length = 32)
    private String author;

    @Column(name = "publisher", nullable = false, length = 32)
    private String publisher;

    @Column(name = "year", nullable = false, length = 4)
    private int year;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<BookLabel> labels;
}
