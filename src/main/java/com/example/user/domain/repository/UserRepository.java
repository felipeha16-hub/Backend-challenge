package com.example.user.domain.repository;

import com.example.user.domain.model.User;

public interface UserRepository {

    User save(User user);

    boolean existsByEmail(String email);



    boolean passwordMatches(String email, String password);

}
