package com.tofu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tofu.demo.Utils;
import com.tofu.demo.model.Book;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.service.dto.BookRequest;
import com.tofu.demo.service.library.LibraryService;
import lombok.SneakyThrows;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Book> books;

    @BeforeEach
    public void setUp() {
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

        bookRepository.saveAll(books);
    }

    @Test
    public void testGetBooks() throws Exception {
        mockMvc.perform(get("/library/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(books.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(books.get(1).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value(books.get(2).getTitle()));
    }

    @Test
    @SneakyThrows
    public void testInsertBook() {
        var request = BookRequest.builder()
                .title("The Illusion of Life")
                .author("Frank Thomas")
                .isbn("0896592324")
                .publisher("Disney Editions")
                .year(1981)
                .build();

        var requestString = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(Utils.matchUuidFormat));
    }


}
