package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.UserType;
import lombok.Builder;
import lombok.Data;

/**
 * Response DTO for returning user data.
 */
@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType userType;
}
