package com.project.base.service.interfaces;

import com.project.base.api.request.RefreshTokenRequest;
import com.project.base.model.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshToken createdRefreshToken(Long id);

    void refreshToken(RefreshTokenRequest request);
}
