package com.example.user.application.useCases;

import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.mapper.NotificationMapper;
import com.example.user.application.mapper.UserMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.Notification;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.NotificationRepository;
import com.example.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class CreateNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SendNotificationUseCase sendNotification;


    public NotificationResponseDTO create(CreateNotificationDTO dto,String email) {

        //Transform DTO a dominio
        Notification notification = NotificationMapper.toDomain(dto);

        // Get user by email
        User user = userRepository.findByEmail(email).get();


        notification.setUser(user);

        //validate that the channel is supported

        if (!sendNotification.isChannelSupported(notification.getChannel())) {
            throw new BusinessException(BusinessErrorMessage.INVALID_NOTIFICATION_CHANNEL);
        }
        // 5. Save to repository
        notification.setStatus("PENDING");

        Notification savedNotification = notificationRepository.save(notification);

        //send notification to channels (Email, SMS and Push Notification)
        sendNotification.sendNotification(savedNotification);






        return NotificationMapper.toDto(savedNotification);

    }


}
