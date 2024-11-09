package com.lab1.common.error;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = List.of(error);
    }

    public void setError(final String error) {
        errors = List.of(error);
    }
}
