package com.getir.library_management_system.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.enums.Genre;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookRequest createTestBookRequest(String isbn) {
        return new CreateBookRequest(
                "Test Book",
                "Test Author",
                isbn,
                LocalDate.of(2020, 1, 1),
                Genre.FICTION,
                10
        );
    }

    @Test
    @Order(1)
    @WithMockUser(authorities = {"LIBRARIAN"})
    void createBook_shouldReturnCreatedBook() throws Exception {
        CreateBookRequest request = createTestBookRequest(UUID.randomUUID().toString());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = {"LIBRARIAN", "PATRON"})
    void getBook_shouldReturnBook() throws Exception {
        String isbn = UUID.randomUUID().toString();
        CreateBookRequest request = createTestBookRequest(isbn);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        Long bookId = json.get("id").asLong();

        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = {"LIBRARIAN"})
    void updateBook_shouldUpdateAndReturnBook() throws Exception {
        String isbn = UUID.randomUUID().toString();
        CreateBookRequest request = createTestBookRequest(isbn);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        Long bookId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        UpdateBookRequest updateRequest = new UpdateBookRequest(
                "Updated Book",
                "Updated Author",
                isbn,
                LocalDate.of(2021, 5, 5),
                Genre.HISTORY,
                5
        );

        mockMvc.perform(put("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    @Order(4)
    @WithMockUser(authorities = {"LIBRARIAN"})
    void deleteBook_shouldDeleteSuccessfully() throws Exception {
        String isbn = UUID.randomUUID().toString();
        CreateBookRequest request = createTestBookRequest(isbn);

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        Long bookId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @WithMockUser(authorities = {"LIBRARIAN"})
    void searchBooks_shouldReturnPaginatedResults() throws Exception {
        for (int i = 0; i < 5; i++) {
            CreateBookRequest request = createTestBookRequest(UUID.randomUUID().toString());
            request.setTitle("Book " + i);
            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/books?keyword=Book&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));
    }
}