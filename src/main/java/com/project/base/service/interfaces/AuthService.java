package com.project.base.service.interfaces;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.ApiResponse;
import com.project.base.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    ApiResponse authenticate(LoginRequest request);

    ApiResponse getRefreshToken(RefreshTokenRequest request);

    ApiResponse logout(HttpServletRequest request, HttpServletResponse response);
}
