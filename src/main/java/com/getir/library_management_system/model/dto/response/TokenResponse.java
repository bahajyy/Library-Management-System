package com.getir.library_management_system.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents JWT token response.
 */
@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
}
