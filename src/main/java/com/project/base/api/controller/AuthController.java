package com.project.base.api.controller;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.ApiResponse;
import com.project.base.request.LoginRequest;
import com.project.base.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse authentication(@Valid @RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public ApiResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.getRefreshToken(request);
    }

    @PostMapping("/logout")
    public ApiResponse logout() {
        return authService.logout();
    }
}
