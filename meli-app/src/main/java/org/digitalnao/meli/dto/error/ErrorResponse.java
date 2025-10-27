package org.digitalnao.meli.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing standardized error responses.
 * This class provides a consistent structure for error information returned
 * by the API, including timestamp, HTTP status code, error type, detailed
 * message, and the request path that triggered the error. It ensures uniform
 * error handling across all endpoints in the application.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}