package com.ecommerce.customerservice.customer.handler;

import com.ecommerce.customerservice.customer.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Global exception handler for the customer service application.
 * <p>
 * This class is annotated with {@link RestControllerAdvice} to provide centralized exception handling
 * across all {@link RestController} components. It captures specific exceptions and returns appropriate
 * HTTP responses.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link CustomerNotFoundException} exceptions.
     * <p>
     * This method is triggered when a {@link CustomerNotFoundException} is thrown in the application.
     * It returns a {@link ResponseEntity} with a 404 NOT FOUND status and the exception message as the response body.
     * </p>
     *
     * @param exp the exception thrown when a customer is not found
     * @return a {@link ResponseEntity} containing the error message and a 404 status
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handle(CustomerNotFoundException exp) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMsg());
    }

    /**
     * Handles {@link MethodArgumentNotValidException} exceptions.
     * <p>
     * This method is triggered when a method argument validation fails. It collects all validation errors,
     * maps them to a field-error message structure, and returns a {@link ResponseEntity} with a 400 BAD REQUEST status.
     * The response body contains an {@link ErrorResponse} object with the validation errors.
     * </p>
     *
     * @param exp the exception thrown when method argument validation fails
     * @return a {@link ResponseEntity} containing the validation errors and a 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(errors));
    }
}