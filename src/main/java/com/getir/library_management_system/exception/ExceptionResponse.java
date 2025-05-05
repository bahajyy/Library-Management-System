package com.getir.library_management_system.exception;

import java.time.LocalDateTime;

/**
 * Represents the structure of error responses returned by the API.
 */
public record ExceptionResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {}
