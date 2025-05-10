package com.getir.library_management_system.model.mapper;

import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Maps CreateUserRequest DTO to User entity
    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userType(request.getUserType())
                .password(request.getPassword())
                .build();
    }

    // Maps User entity to UserResponse DTO
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .build();
    }
}
