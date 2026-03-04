package com.example.user.infrastructure.error_management;

import com.example.user.domain.exceptions.BusinessException;
import com.example.user.infrastructure.error_management.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;




@RestControllerAdvice
@ConditionalOnProperty(name = "app.exception-handler.enabled", havingValue = "true")
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex,
            WebRequest request) {

        var errorMessage = ex.getBusinessErrorMessage();
        var status = errorMessage.getHttpStatus();

        // extract request path for logging
        String path = extractPath(request);

        log.warn("BusinessException capturada: {} - Path: {}", errorMessage.getMessage(), path);

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(System.currentTimeMillis())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(errorMessage.getMessage())
                .path(path)
                .build();


        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    /**
     * Validation error handler (@Valid, @Validated)
     * Captures errors when DTO validations fail:
     * - @Size, @NotNull, @NotBlank, @Email, @Min, @Max, etc.
     * Returns a 400 Bad Request with the validation message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String path = extractPath(request);

        // Get the first validation error
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        log.warn("Validation error: {} - Path: {}", errorMessage, path);

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessage)
                .path(path)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(
            Exception ex,
            HttpServletRequest request) {

        // We determine the status, if the exception is a security exception or the message contains "JWT" or "token", we give 401, otherwise it's a real internal error (500).
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";

        if (ex instanceof org.springframework.security.core.AuthenticationException ||
                ex.getMessage().toLowerCase().contains("jwt") ||
                ex.getMessage().toLowerCase().contains("token")) {
            status = HttpStatus.UNAUTHORIZED;
            message = "Unauthorized: " + ex.getMessage();
        } else {
            log.error("Unhandled exception: {}", ex.getMessage(), ex);
        }

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(System.currentTimeMillis())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }


     //Auxiliary method to extract the request path

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getRequest();
            return httpRequest.getRequestURI();
        }
        return "unknown";
    }
}


