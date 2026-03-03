package com.example.user.domain.repository;

import com.example.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);

    boolean existsByEmail(String email);
}
