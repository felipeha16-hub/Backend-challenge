package com.example.user.infrastructure.adapters.notifications;

import com.example.user.domain.model.Notification;
import com.example.user.domain.repository.NotificationRepository;
import com.example.user.domain.services.NotificationSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class PushNotificationAdpater implements NotificationSenderService {

    private final NotificationRepository notificationRepository;

    @Override
    public boolean supports(String channel) {
        return channel.equals("Push Notification");
    }



    @Override
    public void sendNotification(Notification notification) {

        //token validation


    }
}
