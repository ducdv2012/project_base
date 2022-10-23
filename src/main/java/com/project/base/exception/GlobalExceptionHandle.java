package com.project.base.exception;

import com.project.base.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {
    @ExceptionHandler({NotBearerTokenHandle.class})
    public ApiResponse handleNotBearerToken(final NotBearerTokenHandle ex, final WebRequest request) {
        return new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler({AuthExceptionHandle.class})
    public ApiResponse authExceptionHandle(final NotBearerTokenHandle ex, final WebRequest request) {
        return new ApiResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler({RefreshTokenExceptionHandle.class})
    public ApiResponse refreshTokenHandle(RefreshTokenExceptionHandle ex, WebRequest request) {
        return new ApiResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
