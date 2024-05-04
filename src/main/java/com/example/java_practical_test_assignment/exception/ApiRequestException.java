package com.example.java_practical_test_assignment.exception;
public class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message) {
        super(message);
    }
}
