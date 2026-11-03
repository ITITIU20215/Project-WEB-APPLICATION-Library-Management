package com.library.common;

import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * The single error body returned by every API endpoint.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorItem> fieldErrors) {

    public static ErrorResponse of(HttpStatusCode status, String message, String path) {
        return of(status, message, path, List.of());
    }

    public static ErrorResponse of(HttpStatusCode status, String message, String path,
            List<FieldErrorItem> fieldErrors) {
        HttpStatus resolved = HttpStatus.resolve(status.value());
        String error = resolved != null ? resolved.getReasonPhrase() : String.valueOf(status.value());
        return new ErrorResponse(Instant.now(), status.value(), error, message, path, fieldErrors);
    }
}
