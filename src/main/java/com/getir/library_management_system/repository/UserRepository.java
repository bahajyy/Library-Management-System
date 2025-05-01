package com.getir.library_management_system.repository;

import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndUserType(String email, UserType userType);
}
