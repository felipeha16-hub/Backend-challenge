package com.example.user.infrastructure.security;

import com.example.user.domain.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final HandlerExceptionResolver resolver;


    public JwtAuthenticationFilter(TokenService tokenService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.tokenService = tokenService;
        this.resolver = resolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            //if the header is present and starts with Bearer, we extract the token and validate it
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (tokenService.isTokenValid(token)) {
                    String email = tokenService.extractEmail(token);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            }
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            resolver.resolveException(request, response, null, e);
            return;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/public/");
    }
}
