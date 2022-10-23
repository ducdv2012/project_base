package com.project.base.service.impl;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.RefreshTokenResponse;
import com.project.base.config.JwtTokenUtil;
import com.project.base.exception.RefreshTokenExceptionHandle;
import com.project.base.model.RefreshToken;
import com.project.base.model.Users;
import com.project.base.repository.RefreshTokenRepository;
import com.project.base.repository.UserRepository;
import com.project.base.service.interfaces.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${refresh.token.validity}")
    private Long expiryDate;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public RefreshToken createdRefreshToken(Long id) {
        try {
            Users users = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUsers(users);
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(expiryDate));
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request) {
        try {
            String requestRefreshToken = request.getRefreshToken();

            RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken);
            if (refreshToken == null)
                throw new RefreshTokenExceptionHandle(requestRefreshToken, "Refresh token is not in database");

            Users users = userRepository.findById(refreshToken.getUsers().getId())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            verifyExpiration(refreshToken);
            String token = jwtTokenUtil.generateToken(users);

            RefreshTokenResponse tokenResponse = new RefreshTokenResponse();
            tokenResponse.setAccessToken(token);
            tokenResponse.setRefreshToken(requestRefreshToken);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByUsers(token.getUsers());
            throw new RefreshTokenExceptionHandle(token.getToken(), "Refresh token was expired");
        }
    }
}
