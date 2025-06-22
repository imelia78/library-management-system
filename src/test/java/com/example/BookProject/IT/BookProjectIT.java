package com.example.BookProject.IT;

import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // Имитация http запросов
@ActiveProfiles("test") // В application-test.yml должна быть H2 или test-PostgreSQL
public class BookProjectIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateBookSuccessfully() throws Exception {
        BookDTO book = new BookDTO(null, "Integration Book", "1234567890123", 100,
                10, 10, ReaderLevel.BEGINNER, Collections.singletonList("CS"), Set.of("Author"));

        mockMvc.perform(post("/api/v1/books/save_book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        assertThat(bookRepository.findAll()).hasSize(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldFindBookByTitle() throws Exception {
        BookDTO book = new BookDTO(null, "FindMe", "9876543210123", 120,
                5, 5, ReaderLevel.ADVANCED, Collections.singletonList("AI"), Set.of("Someone"));

        mockMvc.perform(post("/api/v1/books/save_book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/books/by-title/FindMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FindMe"))
                .andExpect(jsonPath("$.isbn").value("9876543210123"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteBookSuccessfully() throws Exception {
        BookDTO book = new BookDTO(null, "ToDelete", "1111111111111", 150,
                7, 7, ReaderLevel.INTERMEDIATE, Collections.singletonList("Networks"), Set.of("DelAuthor"));

        // Сначала сохраняем книгу
        String response = mockMvc.perform(post("/api/v1/books/save_book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookDTO saved = objectMapper.readValue(response, BookDTO.class);

        // Удаляем
        mockMvc.perform(delete("/api/v1/books/delete_book/" + saved.id())
                        .with(csrf()))
                .andExpect(status().isOk());

        assertThat(bookRepository.findById(saved.id())).isEmpty();
    }
}

