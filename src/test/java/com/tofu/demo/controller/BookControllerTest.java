package com.tofu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tofu.demo.Utils;
import com.tofu.demo.model.Book;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.library.LibraryService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Book> books;

    private String randIsbn() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
    @BeforeEach
    void setUp() {
        books = Arrays.asList(
                Book.builder().id(UUID.randomUUID().toString()).isbn(randIsbn())
                        .title("Spirited Away, Vol. 1").author("Hayao Miyazaki")
                        .year(2002).publisher("VIZ LLC")
                        .build(),
                Book.builder().id(UUID.randomUUID().toString()).isbn(randIsbn())
                        .title("Inuyasha Anime Artbook").author("Rumiko Takahashi")
                        .year(2001).publisher("Shogakukan")
                        .build(),
                Book.builder().id(UUID.randomUUID().toString()).isbn(randIsbn())
                        .title("Dragonball (Volume 1)").author("Akira Toriyama")
                        .year(2000).publisher("VIZ LLC")
                        .build()
        );

        bookRepository.saveAll(books);
    }

    @Test
    void testGetBooks() throws Exception {
        mockMvc.perform(get("/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(books.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(books.get(1).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value(books.get(2).getTitle()));
    }

    @Test
    void testGetDetailByIsbn() throws Exception {
        String isbn = books.get(1).getIsbn();

        mockMvc.perform(MockMvcRequestBuilders.get("/book/isbn/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.publisher").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.labels").isArray());
    }

    @Test
    @SneakyThrows
    void testInsertBook() {
        var request = BookRequest.builder()
                .title("The Illusion of Life")
                .author("Frank Thomas")
                .isbn("0896592324")
                .publisher("Disney Editions")
                .year(1981)
                .build();

        var requestString = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(Utils.matchUuidFormat));
    }

    @Test
    void testEditBook() throws Exception {
        String bookId = books.get(1).getId();
        BookRequest bookRequest = BookRequest.builder()
                .isbn("1234567890")
                .title("Updated Title")
                .author("Updated Author")
                .publisher("Updated Publisher")
                .year(2023)
                .build();

        String requestJson = objectMapper.writeValueAsString(bookRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteBook() throws Exception {
        String bookId = books.get(2).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
