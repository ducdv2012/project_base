package com.project.base.api.controller;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.ApiResponse;
import com.project.base.request.LoginRequest;
import com.project.base.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse authentication(@Valid @RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public ApiResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.getRefreshToken(request);
    }

    @PostMapping("/logout")
    public ApiResponse logout(final HttpServletRequest request, final HttpServletResponse response) {
        return authService.logout(request, response);
    }
}
