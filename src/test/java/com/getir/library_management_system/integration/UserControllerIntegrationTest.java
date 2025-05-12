package com.getir.library_management_system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.request.UpdateUserRequest;
import com.getir.library_management_system.model.enums.UserType;
import com.getir.library_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        var user = userRepository.save(
                com.getir.library_management_system.model.entity.User.builder()
                        .name("John")
                        .email("john@example.com")
                        .phone("123456789")
                        .password("password")
                        .userType(UserType.LIBRARIAN)
                        .build()
        );
        userId = user.getId();
    }

    @WithMockUser(username = "admin", roles = {"LIBRARIAN"})
    @Test
    void register_shouldSucceed() throws Exception {
        var request = new CreateUserRequest(
                "Jane", "jane@example.com", "987654321", UserType.PATRON, "1234"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("jane@example.com")));
    }


    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void getUser_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void updateUser_shouldUpdateSuccessfully() throws Exception {
        var request = new UpdateUserRequest("Updated", "updated@example.com", "0000000000", UserType.LIBRARIAN);

        mockMvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void deleteUser_shouldSucceed() throws Exception {
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());
    }
}
