package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.UserType;
import lombok.Data;

/**
 * Represents login request payload.
 */
@Data
public class LoginRequest {
    private String email;
    private UserType userType;
}
