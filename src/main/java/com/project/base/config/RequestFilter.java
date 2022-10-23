package com.project.base.config;

import com.project.base.exception.AuthExceptionHandle;
import com.project.base.exception.NotBearerTokenHandle;
import com.project.base.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.base.util.Constants.PREFIX_HEADER;
import static com.project.base.util.Constants.PREFIX_TOKEN;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(PREFIX_HEADER);
        String token;
        String username;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(PREFIX_TOKEN)) {
            token = requestTokenHeader.substring(PREFIX_TOKEN.length() + 1);
            username = tokenUtil.getClaimFromToken(token, Claims::getSubject);
        } else {
            throw new NotBearerTokenHandle("Token not type Bearer");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            if (!tokenUtil.validateToken(token, userDetails))
                throw new AuthExceptionHandle("Token has been expired");
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
