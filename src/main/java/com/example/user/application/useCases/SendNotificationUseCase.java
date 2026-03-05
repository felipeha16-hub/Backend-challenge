package com.example.user.application.useCases;

import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.Notification;
import com.example.user.domain.services.NotificationSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SendNotificationUseCase {

    private final List<NotificationSenderService> senders;

    public void sendNotification(Notification notification) {
        senders.stream()
                .filter(s -> s.supports(notification.getChannel()))
                .findFirst()
                .ifPresentOrElse(
                        s -> s.sendNotification(notification),
                        () -> { throw new BusinessException(BusinessErrorMessage.INVALID_NOTIFICATION_CHANNEL); }
                );
    }
}


