package com.getir.library_management_system.service;

import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.request.UpdateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;

public interface UserService {

    /**
     * Registers a new user (librarian or patron).
     */
    UserResponse registerUser(CreateUserRequest request);

    /**
     * Returns user details. Only accessible to librarians.
     */
    UserResponse getUser(Long id);

    /**
     * Updates an existing user's information. Only accessible to librarians.
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * Deletes a user by ID. Only accessible to librarians.
     */
    void deleteUser(Long id);
}
