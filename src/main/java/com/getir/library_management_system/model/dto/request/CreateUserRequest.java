package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.UserType;
import lombok.Data;

/**
 * Request DTO for creating a user.
 */
@Data
public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
    private UserType userType;
}
