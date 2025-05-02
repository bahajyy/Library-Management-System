package com.getir.library_management_system.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnBookRequest {

    @NotNull
    private Long borrowingId;
}
