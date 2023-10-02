package com.tofu.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tofu.demo.model.Label;
import com.tofu.demo.repository.LabelRepository;
import com.tofu.demo.service.dto.LabelRequest;
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
class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    List<Label> labels;

    @BeforeEach
    public void setUp() {
        labels = Arrays.asList(
                Label.builder().id("label1").name("Label-1").build(),
                Label.builder().id("label2").name("Label-2").build(),
                Label.builder().id("label3").name("Label-3").build()
        );
        labelRepository.saveAll(labels);
    }

    @Test
    void testGetList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/label")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void testInsertLabel() throws Exception {
        LabelRequest labelRequest = LabelRequest.builder()
                .name("New Label")
                .description("Description of the new label")
                .build();

        String requestJson = objectMapper.writeValueAsString(labelRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/label")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testEditLabel() throws Exception {
        String labelId = labels.get(1).getId();
        LabelRequest labelRequest = LabelRequest.builder()
                .name("Updated Label")
                .description("Updated description")
                .build();

        String requestJson = objectMapper.writeValueAsString(labelRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/label/{id}", labelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteLabel() throws Exception {
        String labelIdToDelete = labels.get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/label/{id}", labelIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
