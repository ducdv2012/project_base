package com.project.base.config;

import com.google.gson.Gson;
import com.project.base.exception.AuthExceptionHandle;
import com.project.base.exception.NotBearerTokenHandle;
import com.project.base.service.impl.AuthServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.project.base.util.Constants.PREFIX_HEADER;
import static com.project.base.util.Constants.PREFIX_TOKEN;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {
    private final AuthServiceImpl authService;
    private final JwtTokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            final String requestTokenHeader = request.getHeader(PREFIX_HEADER);
            String token = null;
            String username = null;
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            if (requestTokenHeader != null) {
                if (requestTokenHeader.startsWith(PREFIX_TOKEN)) {
                    token = requestTokenHeader.substring(PREFIX_TOKEN.length() + 1);
                    username = tokenUtil.getClaimFromToken(token, Claims::getSubject);
                } else {
                    throw new NotBearerTokenHandle("Token not type " + PREFIX_TOKEN);
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authService.loadUserByUsername(username);
                if (!tokenUtil.validateToken(token, userDetails))
                    throw new AuthExceptionHandle("Token has been invalid");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            printErrorResponse(response, "Token has been expired");
        } catch (Exception e) {
            printErrorResponse(response, e.getMessage());
        }
    }

    private void printErrorResponse(HttpServletResponse response, String message) throws IOException {
        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> hm = new HashMap<>();
        hm.put("message", message);
        out.print(parseObjectToString(hm));
        out.flush();
    }

    private String parseObjectToString(Object object) {
        return new Gson().toJson(object);
    }
}
