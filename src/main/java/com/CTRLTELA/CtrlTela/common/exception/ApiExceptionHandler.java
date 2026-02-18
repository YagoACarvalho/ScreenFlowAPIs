package com.CTRLTELA.CtrlTela.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandler {

    private ApiError build(HttpStatus status, String message, ServletWebRequest req, Object details) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequest().getRequestURI(),
                details
        );
    }

    // 400 - @Valid body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            ServletWebRequest req
    ) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                fields.putIfAbsent(fe.getField(), fe.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(HttpStatus.BAD_REQUEST, "Validation error", req, fields));
    }

    // 400 - @RequestParam/@PathVariable validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex,
            ServletWebRequest req
    ) {
        var violations = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(HttpStatus.BAD_REQUEST, "Validation error", req, violations));
    }

    // 400 - regra de negócio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            ServletWebRequest req
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(build(HttpStatus.BAD_REQUEST, safeMsg(ex, "Bad request"), req, null));
    }

    // 401 - auth inválida (Spring Security)
    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    public ResponseEntity<ApiError> handleUnauthorized(
            Exception ex,
            ServletWebRequest req
    ) {
        String msg = (ex instanceof AuthenticationException) ? "Unauthorized" : safeMsg(ex, "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(build(HttpStatus.UNAUTHORIZED, msg, req, null));
    }

    // 403 - forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbidden(
            AccessDeniedException ex,
            ServletWebRequest req
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(build(HttpStatus.FORBIDDEN, "Forbidden", req, null));
    }

    // 404 - not found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            NotFoundException ex,
            ServletWebRequest req
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(build(HttpStatus.NOT_FOUND, safeMsg(ex, "Not found"), req, null));
    }

    // 409 - conflito (unique, fk, etc)
    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConflict(
            Exception ex,
            ServletWebRequest req
    ) {
        if (ex instanceof DataIntegrityViolationException dive) {
            String msg = messageForDataIntegrity(dive);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(build(HttpStatus.CONFLICT, msg, req, null));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(build(HttpStatus.CONFLICT, safeMsg(ex, "Conflict"), req, null));
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            ServletWebRequest req
    ) {
        // Aqui você pode logar ex (server-side), mas não devolve pro cliente
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", req, null));
    }

    private String safeMsg(Exception ex, String fallback) {
        return (ex.getMessage() == null || ex.getMessage().isBlank()) ? fallback : ex.getMessage();
    }

    private String messageForDataIntegrity(DataIntegrityViolationException ex) {
        // Postgres: 23505 unique_violation, 23503 foreign_key_violation
        Throwable t = ex;
        while (t != null) {
            if (t instanceof SQLException sql) {
                String state = sql.getSQLState();
                if ("23505".equals(state)) return "Conflict: duplicate key";
                if ("23503".equals(state)) return "Conflict: foreign key violation";
                return "Conflict";
            }
            t = t.getCause();
        }
        return "Conflict";
    }
}
