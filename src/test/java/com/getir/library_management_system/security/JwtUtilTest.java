package com.getir.library_management_system.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        User user = new User("user@example.com", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String token = jwtUtil.generateJwtToken(authentication);
        assertThat(token).isNotNull();
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
        assertThat(jwtUtil.getUsernameFromJwtToken(token)).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertThat(jwtUtil.validateJwtToken("invalid.token.value")).isFalse();
    }
}
