package com.getir.library_management_system.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret = "my-secret-key-my-secret-key-my-secret-key"; // Must be at least 256-bit
    private final long jwtExpirationMs = 86400000; // Token validity: 1 day

    // Returns the signing key for JWT (HS256)
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generates a JWT token using authenticated user's details
    public String generateJwtToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User userPrincipal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Sets username as subject
                .claim("roles", userPrincipal.getAuthorities()) // Adds roles as claim
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Token expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signing with secret key
                .compact(); // Builds the JWT
    }

    // Extracts username from JWT token
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validates the JWT token's structure and signature
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
