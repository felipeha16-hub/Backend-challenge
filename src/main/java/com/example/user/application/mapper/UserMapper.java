package com.example.user.application.mapper;

import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.domain.model.User;



public class UserMapper {


    public static UserResponseDTO toDTO(String token) {
        return new UserResponseDTO(token);
    }


    public static User toDomain(CreateUserDTO dto) {
        return new User(null, dto.getEmail(), dto.getPassword());
    }


    public static User toDomain(LoginUserDTO dto) {
        return new User(null, dto.getEmail(), dto.getPassword());
    }
}
