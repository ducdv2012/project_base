package com.project.base.service.impl;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.AccessTokenResponse;
import com.project.base.api.response.ApiResponse;
import com.project.base.config.JwtTokenUtil;
import com.project.base.model.RefreshToken;
import com.project.base.model.Users;
import com.project.base.request.LoginRequest;
import com.project.base.service.interfaces.AuthService;
import com.project.base.service.interfaces.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public ApiResponse authenticate(LoginRequest request) {
        try {
            log.info("----- Start login ----");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Users users = (Users) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(users);
            RefreshToken refreshToken = refreshTokenService.createdRefreshToken(users.getId());
            List<String> roles = users.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
            accessTokenResponse.setId(users.getId());
            accessTokenResponse.setEmail(users.getEmail());
            accessTokenResponse.setAccessToken(token);
            accessTokenResponse.setRefreshToken(refreshToken.getToken());
            accessTokenResponse.setRoles(roles);
            log.info("------ Login successfully -----");
            return new ApiResponse(HttpStatus.OK.value(), "Login successfully", accessTokenResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Login failed", e.getMessage());
        }
    }

    @Override
    public ApiResponse getRefreshToken(RefreshTokenRequest request) {
        try {
            log.info("------- Start login with refresh token ------");
            refreshTokenService.refreshToken(request);
            log.info("------- Start login with refresh token successfully ------");
            return new ApiResponse(HttpStatus.OK.value(), "Login with refresh token successfully", null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Login with refresh token failed", e.getMessage());
        }
    }

    @Override
    public ApiResponse logout() {
        return null;
    }
}
