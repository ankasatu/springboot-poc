package com.tofu.demo.repository;

import com.tofu.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);
}
