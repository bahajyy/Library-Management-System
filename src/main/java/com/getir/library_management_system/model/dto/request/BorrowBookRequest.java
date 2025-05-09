package com.getir.library_management_system.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBookRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;
}
