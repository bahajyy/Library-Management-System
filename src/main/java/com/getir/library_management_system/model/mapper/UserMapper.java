package com.getir.library_management_system.model.mapper;

import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.model.entity.User;

/**
 * Maps between User entities and DTOs.
 */
public class UserMapper {

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userType(request.getUserType())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .build();
    }
}
