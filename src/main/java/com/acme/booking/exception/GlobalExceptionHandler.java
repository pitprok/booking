package com.acme.booking.exception;

import jakarta.validation.ConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BookingOverlapException.class)
    public ResponseEntity<String> handleBookingOverlapException(BookingOverlapException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PastBookingException.class)
    public ResponseEntity<String> handlePastBookingException(PastBookingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles validation exceptions thrown when method arguments fail validation constraints.
     *
     * <p>This method catches exceptions of type {@link MethodArgumentNotValidException}, which can occur in two scenarios:
     * <ul>
     *     <li>When a controller method's parameters, annotated with standard validation annotations (e.g., {@code @NotNull},
     *     {@code @Size}), fail validation.</li>
     *     <li>When custom validation logic, implemented using {@link ConstraintValidator}, fails (e.g., validations defined
     *     on DTOs or entities).</li>
     * </ul>
     *
     * <p>The method processes validation errors and formats them into a map of field names and corresponding error messages.
     * If a specific field is associated with the validation error (via {@link FieldError}), the field name is used as the key.
     * Otherwise, a generic "Validation error" key is used for errors not tied to specific fields.
     *
     * @param ex the {@link MethodArgumentNotValidException} containing validation errors
     * @return a {@link ResponseEntity} containing a map of validation errors as the body and an HTTP status of {@code BAD_REQUEST}
     *
     * <p>Example field validation error output:
     * <pre>
     * {
     *   "date": "must not be null",
     * }
     * <p>Example generic validation error output:
     * <pre>
     * {
     *   "Validation error": "The booking duration must be at least 1 hour and in 1-hour increments."
     * }
     * </pre>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put("Validation error", error.getDefaultMessage());
            }
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
