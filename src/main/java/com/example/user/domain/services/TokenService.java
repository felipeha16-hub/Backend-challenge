package com.example.user.domain.services;

import com.example.user.domain.model.User;

public interface TokenService {
    String generateToken(User user);
    boolean isTokenValid(String token);
    String extractEmail(String token);

}
