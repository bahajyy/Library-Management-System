package com.getir.library_management_system.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to verify that the application is running and Swagger works.
 */
@RestController
public class TestController {
    @GetMapping("/api/test")
    public String testEndpoint() {
        return "Library Management System API is working!";
    }
}
