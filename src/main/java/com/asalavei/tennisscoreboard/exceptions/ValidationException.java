package com.asalavei.tennisscoreboard.exceptions;

public class ValidationException extends AppRuntimeException {

    public ValidationException(String message, String pagePath) {
        super(message, pagePath);
    }
}
