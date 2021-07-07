package com.saklecha.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ValidationException extends RuntimeException {

    private String code;
    private String message;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public ValidationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ValidationException(String code) {
        super(code);
        this.code = code;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
