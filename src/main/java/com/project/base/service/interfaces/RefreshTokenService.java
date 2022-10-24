package com.project.base.service.interfaces;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.api.response.RefreshTokenResponse;
import com.project.base.model.RefreshTokens;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshTokens createdRefreshToken(Long id);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
