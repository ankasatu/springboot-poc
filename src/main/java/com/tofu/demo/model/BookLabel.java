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
@Table(name = "book_label", uniqueConstraints = {
        @UniqueConstraint(name="uq_book_label", columnNames = {"book_id", "label_id"})
})
public class BookLabel {
    @Id
    @GenericGenerator(name = "UUID")
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey=@ForeignKey(name = "fk_book"))
    private Book book;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", foreignKey=@ForeignKey(name = "fk_label"))
    private Label label;
}
