package com.example.user.application.useCases;


import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.dto.UpdateNotificationDTO;
import com.example.user.application.mapper.NotificationMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.Notification;
import com.example.user.domain.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UpdateNotificationUseCase {

    private final NotificationRepository notificationRepository;


    public NotificationResponseDTO update(Long id, UpdateNotificationDTO dto) {


        // Find the existing notification by ID
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage.NOTIFICATION_NOT_FOUND));

        // Update the notification fields
        Notification updatedNotification = NotificationMapper.toDomain(dto, existingNotification);

        // Save the updated notification and return
        Notification saved = notificationRepository.save(updatedNotification);
        return NotificationMapper.toDto(saved);
    }

}
