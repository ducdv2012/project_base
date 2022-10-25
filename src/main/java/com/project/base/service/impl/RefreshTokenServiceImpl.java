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

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MILLIS;

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
        refreshTokens.setExpiryDate(LocalDateTime.now().plus(expiryDate, MILLIS));
        refreshTokens = refreshTokenRepository.save(refreshTokens);
        return refreshTokens;
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshTokens refreshTokens = refreshTokenRepository.findByToken(requestRefreshToken);
        if (refreshTokens == null)
            throw new RefreshTokenExceptionHandle(requestRefreshToken, "Refresh token is not in database");

        Users users = userRepository.findById(refreshTokens.getUsers().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        verifyExpiration(refreshTokens);
        String token = jwtTokenUtil.generateToken(users);

        Date expiryDate = jwtTokenUtil.getExpirationDateFromToken(token);
        Tokens tokens = Tokens.builder()
                .token(token)
                .refreshTokens(refreshTokens)
                .users(users)
                .expiryDate(expiryDate)
                .build();
        tokenRepository.save(tokens);

        RefreshTokenResponse tokenResponse = new RefreshTokenResponse();
        tokenResponse.setAccessToken(token);
        tokenResponse.setRefreshToken(requestRefreshToken);
        return tokenResponse;
    }

    @Transactional
    public void verifyExpiration(RefreshTokens token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            List<Tokens> tokensList = tokenRepository.findAllByUsers(token.getUsers());
            List<Tokens> tokenInvalid = tokensList.stream().filter(t -> t.getExpiryDate().before(new Date())).collect(Collectors.toList());
            if (tokenInvalid.equals(tokensList)) {
                List<Long> tokenIds = tokensList.stream().map(Tokens::getId).collect(Collectors.toList());
                tokenRepository.deleteAllByTokens(tokenIds);
                refreshTokenRepository.deleteByUsers(token.getUsers());
            } else {
                List<Long> tokenIds = tokenInvalid.stream().map(Tokens::getId).collect(Collectors.toList());
                tokenRepository.deleteAllByTokens(tokenIds);
            }
            throw new RefreshTokenExceptionHandle(token.getToken(), "Refresh token was expired");
        }
    }
}
