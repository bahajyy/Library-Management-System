package com.getir.library_management_system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.BorrowingStatus;
import com.getir.library_management_system.model.enums.Genre;
import com.getir.library_management_system.model.enums.UserType;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.repository.BorrowingRepository;
import com.getir.library_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BorrowingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowingRepository borrowingRepository;

    private Long bookId;
    private Long userId;
    private Long borrowingId;

    @BeforeEach
    void setup() {
        borrowingRepository.deleteAll(); // önce borrowing kayıtlarını sil
        bookRepository.deleteAll();
        userRepository.deleteAll();

        Book book = Book.builder()
                .title("Test Book")
                .author("Author")
                .isbn("isbn-123")
                .publicationDate(LocalDate.now().minusYears(1))
                .genre(Genre.FICTION)
                .stock(5)
                .build();
        bookId = bookRepository.save(book).getId();

        User user = User.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .userType(UserType.PATRON)
                .phone("1234567890")
                .build();
        userId = userRepository.save(user).getId();
    }

    @Test
    @WithMockUser(username = "john@example.com", authorities = {"PATRON"})
    void borrowAndReturnBook_shouldSucceed() throws Exception {
        BorrowBookRequest request = new BorrowBookRequest(bookId, userId);

        // borrow book
        String response = mockMvc.perform(post("/api/borrowings/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andReturn().getResponse().getContentAsString();

        borrowingId = objectMapper.readTree(response).get("id").asLong();

        // return book
        mockMvc.perform(post("/api/borrowings/return/" + borrowingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(BorrowingStatus.RETURNED.toString()));
    }

    @Test
    @WithMockUser(username = "john@example.com", authorities = {"PATRON"})
    void myHistory_shouldReturnEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/borrowings/my-history/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = {"LIBRARIAN"})
    void allBorrowings_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/borrowings/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"LIBRARIAN"})
    void overdueEndpoints_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/borrowings/overdue")).andExpect(status().isOk());
        mockMvc.perform(get("/api/borrowings/overdueBooks")).andExpect(status().isOk());
        mockMvc.perform(get("/api/borrowings/overdueBooks/report")).andExpect(status().isOk());
    }
}