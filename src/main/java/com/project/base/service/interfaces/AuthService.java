package com.project.base.service.interfaces;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.ApiResponse;
import com.project.base.request.LoginRequest;

public interface AuthService {
    ApiResponse authenticate(LoginRequest request);

    ApiResponse getRefreshToken(RefreshTokenRequest request);

    ApiResponse logout();
}
