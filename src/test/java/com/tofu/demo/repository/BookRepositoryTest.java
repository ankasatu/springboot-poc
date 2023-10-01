package com.tofu.demo.repository;

import com.tofu.demo.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private List<Book> books;

    @BeforeEach
    private void beforeEach() {
        books = Arrays.asList(
                Book.builder().id("9255182e-fdec-40ba-954d-5baf70bc82c6").isbn("1569317917")
                        .title("Spirited Away, Vol. 1").author("Hayao Miyazaki")
                        .year(2002).publisher("VIZ LLC")
                        .build(),
                Book.builder().id("2e982e59-6fcc-48d6-a20b-f87e1c54fd87").isbn("4091011896")
                        .title("Inuyasha Anime Artbook").author("Rumiko Takahashi")
                        .year(2001).publisher("Shogakukan")
                        .build(),
                Book.builder().id("cda0b064-0ea6-44b3-80c8-a7ae59ad3ea7").isbn("1569314950")
                        .title("Dragonball (Volume 1)").author("Akira Toriyama")
                        .year(2000).publisher("VIZ LLC")
                        .build()
        );

        books.stream().forEach(b->entityManager.persist(b));

        entityManager.flush();
    }

    @Test
    public void testShouldFoundFindByIsbn() {
        Optional<Book> book = bookRepository.findByIsbn("4091011896");

        assertTrue(book.isPresent());
    }

    @Test
    public void testShouldNotFoundFindByIsbn() {
        Optional<Book> book = bookRepository.findByIsbn("1234567890");

        assertFalse(book.isPresent());
    }

}
