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


    public boolean isChannelSupported(String channel) {
        return senders.stream()
                .anyMatch(adapter -> adapter.supports(channel));
    }

    public void sendNotification(Notification notification) {
        senders.stream()
                .filter(adapter -> adapter.supports(notification.getChannel()))
                .findFirst()
                .ifPresent(adapter -> adapter.sendNotification(notification));
    }
}


