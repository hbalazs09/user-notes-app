package com.horvath.usernotesapp.api.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleEntityNotFound_shouldReturnNotFoundResponse() {
        EntityNotFoundException ex = new EntityNotFoundException("Not found");
        ResponseEntity<ErrorResponse> response = handler.handleEntityNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleOptimisticLocking_shouldReturnConflictResponse() {
        OptimisticLockException ex = new OptimisticLockException("Lock failed");
        ResponseEntity<ErrorResponse> response = handler.handleOptimisticLocking(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Concurrent modification error. Please try again.",
                Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleObjectOptimisticLockingFailure_shouldReturnConflictResponse() {
        ObjectOptimisticLockingFailureException ex = new ObjectOptimisticLockingFailureException("Note", 1L);
        ResponseEntity<ErrorResponse> response = handler.handleObjectOptimisticLockingFailure(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Concurrent modification error. Please try again.",
                Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleConstraintViolations_shouldReturnBadRequest() {
        ConstraintViolationException ex = new ConstraintViolationException("Invalid value", Set.of());
        ResponseEntity<ErrorResponse> response = handler.handleConstraintViolations(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getError().contains("Validation failed:"));
    }

    @Test
    void handleTypeMismatch_shouldReturnBadRequest() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("userId");
        ResponseEntity<ErrorResponse> response = handler.handleTypeMismatch(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getError().contains("userId"));
    }

    @Test
    void handleNotReadableException_shouldReturnBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Unreadable", (HttpInputMessage) null);
        ResponseEntity<ErrorResponse> response = handler.handleNotReadableException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Message body is invalid.", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleGeneric_shouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected");
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected", Objects.requireNonNull(response.getBody()).getError());
    }

    @Test
    void handleValidationErrors_shouldReturnBadRequestWithFieldErrors() {
        FieldError fieldError = new FieldError("object", "text", "must not be blank");
        BindException bindException = new BindException(new Object(), "noteDto");
        bindException.addError(fieldError);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindException.getBindingResult());

        ResponseEntity<ValidationErrorResponse> response = handler.handleValidationErrors(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getFieldErrors().containsKey("text"));
        assertEquals("must not be blank", response.getBody().getFieldErrors().get("text"));
    }
}
