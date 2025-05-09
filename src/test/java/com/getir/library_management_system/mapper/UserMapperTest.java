package com.getir.library_management_system.mapper;

import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.UserType;
import com.getir.library_management_system.model.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toEntity_shouldMapCorrectly() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("Jane", "jane@example.com", "123456789", UserType.PATRON, "password123");

        // Act
        User user = UserMapper.toEntity(request);

        // Assert
        assertEquals("Jane", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("123456789", user.getPhone());
        assertEquals(UserType.PATRON, user.getUserType());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void toResponse_shouldMapCorrectly() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Jane")
                .email("jane@example.com")
                .phone("123456789")
                .userType(UserType.PATRON)
                .build();

        // Act
        UserResponse response = userMapper.toResponse(user);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Jane", response.getName());
        assertEquals("jane@example.com", response.getEmail());
        assertEquals("123456789", response.getPhone());
        assertEquals(UserType.PATRON, response.getUserType());
    }
}
