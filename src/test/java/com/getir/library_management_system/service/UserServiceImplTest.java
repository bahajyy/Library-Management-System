package com.getir.library_management_system.service;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.exception.ResourceNotFoundException;
import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.request.UpdateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.UserType;
import com.getir.library_management_system.model.mapper.UserMapper;
import com.getir.library_management_system.repository.UserRepository;
import com.getir.library_management_system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CreateUserRequest createRequest() {
        return new CreateUserRequest("John", "john@example.com", "123", UserType.LIBRARIAN, "rawPass");
    }

    private UpdateUserRequest updateRequest() {
        return new UpdateUserRequest("Jane Doe", "jane@example.com","123" ,UserType.LIBRARIAN);
    }

    @Test
    void registerUser_success() {
        CreateUserRequest req = createRequest();

        User entity = UserMapper.toEntity(req); //

        User saved = new User();
        saved.setId(1L);
        saved.setName(req.getName());
        saved.setEmail(req.getEmail());
        saved.setPhone(req.getPhone());
        saved.setUserType(req.getUserType());
        saved.setPassword("encodedPass");

        UserResponse response = new UserResponse(1L, req.getName(), req.getEmail(), req.getPhone(), req.getUserType());

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(req.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toResponse(any(User.class))).thenReturn(response); // kritik düzeltme

        UserResponse result = userService.registerUser(req);

        assertEquals(response, result);
    }



    @Test
    void registerUser_emailExists_throwsBusinessException() {
        CreateUserRequest req = createRequest();
        when(userRepository.existsByEmail(req.getEmail())).thenReturn(true);
        assertThrows(BusinessException.class, () -> userService.registerUser(req));
    }

    @Test
    void getUser_success() {
        User user = new User(1L, "User", "email", "123", UserType.PATRON, "pass");
        UserResponse response = new UserResponse(1L, "User", "email", "123", UserType.PATRON);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        assertEquals(response, userService.getUser(1L));
    }

    @Test
    void getUser_notFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void updateUser_success() {
        User user = new User(1L, "Old", "old@example.com", "111", UserType.PATRON, "pass");

        UpdateUserRequest req = updateRequest();
        UserResponse response = new UserResponse(1L, req.getName(), req.getEmail(), "111", req.getUserType());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.updateUser(1L, req);

        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals(UserType.LIBRARIAN, user.getUserType());
        assertEquals(response, result);
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, updateRequest()));
    }

    @Test
    void deleteUser_success() {
        User user = new User(1L, "Del", "del@example.com", "000", UserType.PATRON, "pass");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void loadUserByUsername_returnsNull() {
        assertNull(userService.loadUserByUsername("any"));
    }
}
