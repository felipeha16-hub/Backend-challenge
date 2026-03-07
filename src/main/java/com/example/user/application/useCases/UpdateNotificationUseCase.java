package com.example.user.application.useCases;


import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.dto.UpdateNotificationDTO;
import com.example.user.application.mapper.NotificationMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.Notification;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.NotificationRepository;
import com.example.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UpdateNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository repository;
    private final SendNotificationUseCase sendNotification;

    public NotificationResponseDTO update(Long id, UpdateNotificationDTO dto) {


        // Find the existing notification by ID and  if it belongs to that user


        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage.NOTIFICATION_NOT_FOUND));


        // Get email from token
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the notification belongs to the user
        if (!existingNotification.getUser().getEmail().equals(emailFromToken)) {
            throw new BusinessException(BusinessErrorMessage.NOTIFICATION_NOT_FOUND);
        }
        // Validate that the channel is supported if it's being updated
        if ( dto.getChannel() == null ||!sendNotification.isChannelSupported(dto.getChannel())) {
            throw new BusinessException(BusinessErrorMessage.INVALID_NOTIFICATION_CHANNEL);
        }


        // Update the notification fields
        Notification updatedNotification = NotificationMapper.toDomain(dto, existingNotification);

        // Save the updated notification and return
        Notification saved = notificationRepository.save(updatedNotification);
        return NotificationMapper.toDto(saved);
    }

}
