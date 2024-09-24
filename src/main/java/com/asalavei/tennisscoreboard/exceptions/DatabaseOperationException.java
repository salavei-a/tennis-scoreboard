package com.asalavei.tennisscoreboard.exceptions;

public class DatabaseOperationException extends AppRuntimeException {

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
