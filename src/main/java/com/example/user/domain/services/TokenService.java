package com.example.user.domain.services;

import com.example.user.domain.model.User;

import java.security.Key;

public interface TokenService {
    String generateToken(User user);
    boolean isTokenValid(String token);

    Key getSignInKey();
    // String extractEmail(String token);

}
