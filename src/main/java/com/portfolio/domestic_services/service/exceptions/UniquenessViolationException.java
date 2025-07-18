package com.portfolio.domestic_services.service.exceptions;

public class UniquenessViolationException extends RuntimeException {
    public UniquenessViolationException(String message) {
        super(message);
    }
}
