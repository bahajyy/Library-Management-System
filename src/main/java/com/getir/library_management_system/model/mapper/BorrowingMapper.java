package com.getir.library_management_system.model.mapper;

import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.entity.Borrowing;
import org.springframework.stereotype.Component;

@Component
public class BorrowingMapper {
    // Maps Borrowing entity to BorrowingResponse DTO
    public BorrowingResponse toResponse(Borrowing borrowing) {
        return BorrowingResponse.builder()
                .id(borrowing.getId())
                .userId(borrowing.getUser().getId())
                .bookId(borrowing.getBook().getId())
                .bookTitle(borrowing.getBook().getTitle())
                .userEmail(borrowing.getUser().getEmail())
                .borrowDate(borrowing.getBorrowDate())
                .dueDate(borrowing.getDueDate())
                .returnDate(borrowing.getReturnDate())
                .status(borrowing.getStatus())
                .build();
    }
}
