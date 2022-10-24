package com.project.base.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenResponse {
    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public AccessTokenResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
