package com.project.base.exception;

import javax.security.sasl.AuthenticationException;

public class NotBearerTokenHandle extends AuthenticationException {
    public NotBearerTokenHandle(String message) {
        super(message);
    }
}
