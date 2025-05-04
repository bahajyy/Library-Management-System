package com.getir.library_management_system.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverdueBookResponse {
    private Long borrowingId;
    private String bookTitle;
    private String borrowerName;
    private LocalDate dueDate;
    private long daysOverdue;
}
