package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.UserType;
import lombok.*;

/**
 * Response DTO for returning user data.
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType userType;
}
