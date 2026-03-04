package com.example.user.application.mapper;

import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.domain.model.Notification;
import com.example.user.domain.model.User;
import com.example.user.infrastructure.persistence.entity.NotificationEntity;


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
