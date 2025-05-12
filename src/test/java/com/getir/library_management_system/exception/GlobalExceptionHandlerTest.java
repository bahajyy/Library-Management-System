package com.getir.library_management_system.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    void handleNotFound_shouldReturn404() {
        var response = handler.handleNotFound(new ResourceNotFoundException("Not found"), request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().getMessage());
    }

    @Test
    void handleBadRequest_shouldReturn400() {
        var response = handler.handleBadRequest(new BadRequestException("Bad request"), request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorized_shouldReturn401() {
        var response = handler.handleUnauthorized(new UnauthorizedException("Unauthorized"), request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody().getMessage());
    }

    @Test
    void handleConflict_shouldReturn409() {
        var response = handler.handleConflict(new ConflictException("Conflict"), request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().getMessage());
    }

    @Test
    void handleForbidden_shouldReturn403() {
        var response = handler.handleForbidden(new ForbiddenException("Forbidden"), request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody().getMessage());
    }

    @Test
    void handleValidation_shouldReturnFieldErrors() {
        var exception = mock(org.springframework.web.bind.MethodArgumentNotValidException.class);
        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        var fieldError = mock(org.springframework.validation.FieldError.class);

        when(fieldError.getField()).thenReturn("email");
        when(fieldError.getDefaultMessage()).thenReturn("must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidation(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be blank", response.getBody().get("email"));
    }

    @Test
    void handleGeneric_shouldReturn500() {
        var response = handler.handleGeneric(new RuntimeException("Unexpected failure"), request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Unexpected failure"));
    }
}
