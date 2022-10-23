package com.project.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenExceptionHandle extends RuntimeException {
    public RefreshTokenExceptionHandle(String token, String message) {
        super(String.format("Failed [%s]: %s", token, message));
    }
}
