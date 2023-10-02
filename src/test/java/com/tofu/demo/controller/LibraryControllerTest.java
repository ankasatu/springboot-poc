package com.tofu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tofu.demo.model.Book;
import com.tofu.demo.model.BookLabel;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.BookLabelRepository;
import com.tofu.demo.repository.BookRepository;
import com.tofu.demo.repository.LabelRepository;
import com.tofu.demo.service.library.LibraryService;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private BookLabelRepository bookLabelRepository;


    @Autowired
    private LibraryService libraryService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Book> books;
    List<Label> labels;
    List<BookLabel> bookLabels;

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

        labels = Arrays.asList(
                Label.builder().id("label1").name("Label-1").build(),
                Label.builder().id("label2").name("Label-2").build(),
                Label.builder().id("label3").name("Label-3").build()
        );
        labelRepository.saveAll(labels);

        bookLabels = Arrays.asList(
                BookLabel.builder()
                        .id("rel1").label(labels.get(0)).book(books.get(2))
                        .build()
        );
        bookLabelRepository.saveAll(bookLabels);

    }


    @Test
    void testSetLabelForBook() throws Exception {
        String bookId = books.get(0).getId();
        List<String> labelIds = Arrays.asList("label1", "label2", "label3");

        String labelIdsJson = objectMapper.writeValueAsString(labelIds);

        mockMvc.perform(MockMvcRequestBuilders.post("/library/assign/book/{id}/labels", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(labelIdsJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetBooksByLabelId() throws Exception {
        String labelId = labels.get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/library/label/{labelId}/book", labelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.label").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.books").isArray());
    }
}
