package com.tofu.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_label")
public class BookLabel {
    @Id
    @GenericGenerator(name = "UUID")
    private String id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey=@ForeignKey(name = "fk_book"))
    private Book book;
}
