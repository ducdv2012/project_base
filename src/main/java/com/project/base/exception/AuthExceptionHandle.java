package com.project.base.exception;

import javax.security.sasl.AuthenticationException;

public class AuthExceptionHandle extends AuthenticationException {
    public AuthExceptionHandle(String message) {
        super(message);
    }
}
