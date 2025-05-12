package com.getir.library_management_system.service;

import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.UserType;
import com.getir.library_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_success() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("encodedPass");
        user.setUserType(UserType.LIBRARIAN);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("john@example.com");

        assertEquals("john@example.com", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("LIBRARIAN")));
    }

    @Test
    void loadUserByUsername_notFound_throwsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("notfound@example.com"));
    }
}
