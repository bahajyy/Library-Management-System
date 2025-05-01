package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.LoginRequest;
import com.getir.library_management_system.model.dto.response.TokenResponse;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.repository.UserRepository;
import com.getir.library_management_system.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user login and token generation.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmailAndUserType(request.getEmail(), request.getUserType())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getUserType().name());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
