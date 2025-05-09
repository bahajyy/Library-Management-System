package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a user.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
    private UserType userType;
    private String password;
}
