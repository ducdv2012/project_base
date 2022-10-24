package com.project.base.service.impl;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.AccessTokenResponse;
import com.project.base.api.response.ApiResponse;
import com.project.base.api.response.RefreshTokenResponse;
import com.project.base.config.JwtTokenUtil;
import com.project.base.exception.AuthExceptionHandle;
import com.project.base.model.RefreshTokens;
import com.project.base.model.Tokens;
import com.project.base.model.Users;
import com.project.base.repository.TokenRepository;
import com.project.base.repository.UserRepository;
import com.project.base.request.LoginRequest;
import com.project.base.service.interfaces.AuthService;
import com.project.base.service.interfaces.RefreshTokenService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.base.util.Constants.PREFIX_HEADER;
import static com.project.base.util.Constants.PREFIX_TOKEN;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, RefreshTokenService refreshTokenService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
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
            RefreshTokens refreshTokens = refreshTokenService.createdRefreshToken(users.getId());
            List<String> roles = users.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder()
                    .type("Bearer")
                    .id(users.getId())
                    .accessToken(token)
                    .refreshToken(refreshTokens.getToken())
                    .username(users.getUsername())
                    .email(users.getEmail())
                    .roles(roles)
                    .build();

            Tokens tokens = Tokens.builder()
                    .token(token)
                    .refreshTokens(refreshTokens)
                    .users(users)
                    .build();
            tokenRepository.save(tokens);
            log.info("------ Login successfully -----");
            return new ApiResponse(HttpStatus.OK.value(), "Login successfully", accessTokenResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Override
    public ApiResponse getRefreshToken(RefreshTokenRequest request) {
        try {
            log.info("------- Start login with refresh token ------");
            RefreshTokenResponse response = refreshTokenService.refreshToken(request);
            log.info("------- Start login with refresh token successfully ------");
            return new ApiResponse(HttpStatus.OK.value(), "Login with refresh token successfully", response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Override
    public ApiResponse logout(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, auth);

            final String requestTokenHeader = request.getHeader(PREFIX_HEADER);
            String token = requestTokenHeader.substring(PREFIX_TOKEN.length() + 1);
            Tokens tokens = tokenRepository.findByToken(token);
            if (tokens != null)
                tokenRepository.deleteToken(tokens.getId());
            return new ApiResponse(HttpStatus.OK.value(), "Logged out successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
