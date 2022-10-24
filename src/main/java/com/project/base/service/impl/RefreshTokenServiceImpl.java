package com.project.base.service.impl;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.RefreshTokenResponse;
import com.project.base.config.JwtTokenUtil;
import com.project.base.exception.RefreshTokenExceptionHandle;
import com.project.base.model.RefreshTokens;
import com.project.base.model.Tokens;
import com.project.base.model.Users;
import com.project.base.repository.RefreshTokenRepository;
import com.project.base.repository.TokenRepository;
import com.project.base.repository.UserRepository;
import com.project.base.service.interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${refresh.token.validity}")
    private Long expiryDate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public RefreshTokens createdRefreshToken(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        RefreshTokens refreshTokens = new RefreshTokens();
        refreshTokens.setUsers(users);
        refreshTokens.setToken(UUID.randomUUID().toString());
        refreshTokens.setExpiryDate(LocalDateTime.now().plusSeconds(expiryDate));
        refreshTokens = refreshTokenRepository.save(refreshTokens);
        return refreshTokens;
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshTokens refreshTokens = refreshTokenRepository.findByToken(requestRefreshToken);
        if (refreshTokens == null)
            throw new RefreshTokenExceptionHandle(requestRefreshToken, "Refresh token is not in database");

        Users users = userRepository.findById(refreshTokens.getUsers().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        verifyExpiration(refreshTokens);
        String token = jwtTokenUtil.generateToken(users);

        Tokens tokens = Tokens.builder()
                .token(token)
                .refreshTokens(refreshTokens)
                .users(users)
                .build();
        tokenRepository.save(tokens);

        RefreshTokenResponse tokenResponse = new RefreshTokenResponse();
        tokenResponse.setAccessToken(token);
        tokenResponse.setRefreshToken(requestRefreshToken);
        return tokenResponse;
    }

    private void verifyExpiration(RefreshTokens token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUsers(token.getUsers());
            throw new RefreshTokenExceptionHandle(token.getToken(), "Refresh token was expired");
        }
    }
}
