package com.example.user.application.useCases;


import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.mapper.NotificationMapper;
import com.example.user.domain.repository.NotificationRepository;
import com.example.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class GetNotificationsUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponseDTO> getAllNotifications() {


        // Get email from token
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByEmail(emailFromToken).get().getId();
        System.out.println("User id from token: "+ id);
        return notificationRepository.findAllById(id).stream()
                .map(NotificationMapper::toDto)
                .toList();

    }
}
