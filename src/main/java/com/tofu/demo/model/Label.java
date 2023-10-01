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
@Table(name = "label")
public class Label {
    @Id
    @GenericGenerator(name = "UUID")
    private String id;

    @Column(name = "name", nullable = false, length = 64, unique = true)
    private String name;

    @Column(name = "description", length = 128)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "label")
    private List<BookLabel> bookLabels;
}
