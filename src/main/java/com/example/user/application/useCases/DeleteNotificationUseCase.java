package com.example.user.application.useCases;


import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.Notification;
import com.example.user.domain.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DeleteNotificationUseCase {

    private final NotificationRepository notificationRepository;


    public void delete(Long notificationId) {
        Notification existingNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage.NOTIFICATION_NOT_FOUND));




        // Get email from token
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the notification belongs to the user
        if (!existingNotification.getUser().getEmail().equals(emailFromToken)) {
            throw new BusinessException(BusinessErrorMessage.NOTIFICATION_NOT_FOUND);
        }


        notificationRepository.deleteById(notificationId);
    }

}
