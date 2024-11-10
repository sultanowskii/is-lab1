package com.lab1.common.error;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class Lab1ExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAnyException(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getLocalizedMessage(),
            "Unexpected exception:" + ex
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException ex,
        final HttpHeaders headers,
        final HttpStatusCode status,
        final WebRequest request
    ) {
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }
    
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.UNAUTHORIZED,
            ex.getLocalizedMessage(),
            "Authentication failed"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.FORBIDDEN,
            ex.getLocalizedMessage(),
            "You don't have permissions to perform this action"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND,
            ex.getLocalizedMessage(),
            "Resource not found"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ PermissionDeniedException.class })
    public ResponseEntity<Object> handlePermissionDenied(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.FORBIDDEN,
            ex.getLocalizedMessage(),
            "You don't have permissions to perform this action"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(Exception ex) {
        final ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            ex.getLocalizedMessage(),
            "Bad request"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
