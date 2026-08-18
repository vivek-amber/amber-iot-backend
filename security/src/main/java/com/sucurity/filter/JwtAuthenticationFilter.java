package com.sucurity.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sucurity.constants.SecurityConstants;
import com.sucurity.exception.InvalidTokenException;
import com.sucurity.jwt.JwtUtil;
import com.sucurity.model.AuthUser;
import com.sucurity.model.SecurityPrincipal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/user/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(SecurityConstants.AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            jwtUtil.validateToken(token);
        } catch (InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            
            AuthUser authUser = jwtUtil.getAuthUser(token);
            SecurityPrincipal principal = new SecurityPrincipal(authUser);
    
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    principal.getAuthorities());
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
