package com.tofu.demo.repository;

import com.tofu.demo.model.BookLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLabelRepository extends JpaRepository<BookLabel, String> {
}
