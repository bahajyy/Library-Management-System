package com.getir.library_management_system.mapper;

import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.entity.Borrowing;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.BorrowingStatus;
import com.getir.library_management_system.model.mapper.BorrowingMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ActiveProfiles("test")
class BorrowingMapperTest {

    private final BorrowingMapper borrowingMapper = new BorrowingMapper();

    @Test
    void toResponse_shouldMapCorrectly() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .build();

        Book book = Book.builder()
                .id(2L)
                .title("Test Book")
                .build();

        Borrowing borrowing = Borrowing.builder()
                .id(3L)
                .user(user)
                .book(book)
                .borrowDate(LocalDate.of(2024, 1, 1))
                .dueDate(LocalDate.of(2024, 1, 10))
                .returnDate(LocalDate.of(2024, 1, 9))
                .status(BorrowingStatus.RETURNED)
                .build();

        // Act
        BorrowingResponse response = borrowingMapper.toResponse(borrowing);

        // Assert
        assertEquals(3L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(2L, response.getBookId());
        assertEquals("Test Book", response.getBookTitle());
        assertEquals("user@example.com", response.getUserEmail());
        assertEquals(LocalDate.of(2024, 1, 1), response.getBorrowDate());
        assertEquals(LocalDate.of(2024, 1, 10), response.getDueDate());
        assertEquals(LocalDate.of(2024, 1, 9), response.getReturnDate());
        assertEquals(BorrowingStatus.RETURNED, response.getStatus());
    }
}
