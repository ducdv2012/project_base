package com.project.base.service.impl;

import com.project.base.config.JwtTokenUtil;
import com.project.base.util.converter.ParserToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.project.base.util.Constants.PREFIX_HEADER;

@Slf4j
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Optional<Long> getCurrentAuditor() {
        String authorization = request.getHeader(PREFIX_HEADER);
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null && StringUtils.isNotBlank(authorization)) {
//                long userId = ParserToken.getUserId(authorization);
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(authorization.substring(7));
                String userId = claims.get("id").toString();
                return Optional.of(Long.parseLong(userId));
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
