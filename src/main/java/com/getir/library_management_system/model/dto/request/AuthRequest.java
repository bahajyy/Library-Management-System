package com.getir.library_management_system.model.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String userType; // LIBRARIAN veya PATRON
}
