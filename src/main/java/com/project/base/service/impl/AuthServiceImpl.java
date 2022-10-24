package com.project.base.service.impl;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.AccessTokenResponse;
import com.project.base.api.response.ApiResponse;
import com.project.base.config.JwtTokenUtil;
import com.project.base.exception.AuthExceptionHandle;
import com.project.base.model.RefreshToken;
import com.project.base.model.Users;
import com.project.base.repository.UserRepository;
import com.project.base.request.LoginRequest;
import com.project.base.service.interfaces.AuthService;
import com.project.base.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);
        if (users == null)
            throw new AuthExceptionHandle("Username not found");
        return users;
    }

    @Override
    @Transactional
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
            accessTokenResponse.setUsername(users.getUsername());
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
