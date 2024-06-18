package com.example.java_practical_test_assignment.exception;

public class ApiRequestException extends RuntimeException {

    public ApiRequestException() {
        super();
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(Throwable cause) {
        super(cause);
    }
}
