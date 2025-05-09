package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.BorrowingStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BorrowingResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private String userEmail;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowingStatus status;
}
