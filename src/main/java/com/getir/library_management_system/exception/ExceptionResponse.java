package com.getir.library_management_system.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents the structure of error responses returned by the API.
 */
@Data
@Builder
public class ExceptionResponse{
    int status;
    String error;
    String message;
    String path;
    LocalDateTime timestamp;
}


