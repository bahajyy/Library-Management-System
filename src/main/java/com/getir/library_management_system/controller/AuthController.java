package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.LoginRequest;
import com.getir.library_management_system.model.dto.response.JwtResponse;
import com.getir.library_management_system.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtUtil.generateJwtToken(authentication);
            log.info("Authentication successful for email: {}", request.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for email: {}", request.getEmail());
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
