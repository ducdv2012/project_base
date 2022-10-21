package com.project.base.response.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public class ApiResponse extends ResponseEntity<ApiResponse.Payload> {
    public ApiResponse(int code, String message) {
        super(new Payload(code, message, null), HttpStatus.OK);
    }

    public ApiResponse(int code, String message, Object data) {
        super(new Payload(code, message, data), HttpStatus.OK);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Payload {
        private int httpCode;
        private String message;
        private Object data;
    }
}
