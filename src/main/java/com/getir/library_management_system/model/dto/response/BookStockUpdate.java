package com.getir.library_management_system.model.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStockUpdate {
    private Long bookId;
    private String title;
    private Integer stock;
}
