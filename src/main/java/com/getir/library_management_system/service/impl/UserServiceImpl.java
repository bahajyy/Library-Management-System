package com.getir.library_management_system.service.impl;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.exception.ResourceNotFoundException;
import com.getir.library_management_system.model.dto.request.CreateUserRequest;
import com.getir.library_management_system.model.dto.request.UpdateUserRequest;
import com.getir.library_management_system.model.dto.response.UserResponse;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.mapper.UserMapper;
import com.getir.library_management_system.repository.UserRepository;
import com.getir.library_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for user-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use.");
        }
        log.info("Registering new user with email: {}", request.getEmail());
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        log.info("User registered successfully with ID: {}", user.getId());
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUser(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for update with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserType(request.getUserType());
        userRepository.save(user);
        log.info("User updated successfully with ID: {}", id);

        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for deletion with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Bu metod kullanılmıyor, log eklenmiş haliyle aşağıya bırakıldı.
        log.warn("loadUserByUsername() not implemented. Username: {}", username);
        return null;
    }
}
