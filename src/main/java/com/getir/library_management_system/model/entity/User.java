package com.getir.library_management_system.model.entity;

import com.getir.library_management_system.model.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user of the system (either librarian or patron).
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    private String password;

}
