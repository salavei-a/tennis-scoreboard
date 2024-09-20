package com.asalavei.tennisscoreboard.exceptions;

import lombok.Getter;

@Getter
public class AppRuntimeException extends RuntimeException {

    private final String pagePath;

    public AppRuntimeException(String message) {
        super(message);
        this.pagePath = null;
    }

    public AppRuntimeException(String message, String pagePath) {
        super(message);
        this.pagePath = pagePath;
    }
}
