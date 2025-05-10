package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.request.UpdateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles user registration and user management.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j // Enables logging
public class UserController {

    private final UserService userService;

    // Anyone can register (no authorization)
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        UserResponse response = userService.registerUser(request);
        log.info("User registered successfully with id: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    // Only LIBRARIAN can view a user
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can fetch user details
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    // Only LIBRARIAN can update a user
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can update users
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // Only LIBRARIAN can delete a user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can delete users
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
