package com.getir.library_management_system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to verify that the application is running and Swagger works.
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("/api/test")
    public String testEndpoint() {
        log.info("Test endpoint called - Application is up and running.");
        return "Library Management System API is working!";
    }
}
