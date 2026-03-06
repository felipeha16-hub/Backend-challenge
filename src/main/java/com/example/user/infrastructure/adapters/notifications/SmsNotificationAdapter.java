package com.example.user.infrastructure.adapters.notifications;

import com.example.user.domain.model.Notification;
import com.example.user.domain.services.NotificationSenderService;
import com.example.user.infrastructure.persistence.adapter.NotificationRepositoryAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class SmsNotificationAdapter implements NotificationSenderService {

    private final NotificationRepositoryAdapter notificationRepositoryAdapter;

    @Override
    public boolean supports(String channel) {
        return channel.equals("SMS");
    }

    @Override
    public void sendNotification(Notification notification) {

        String content = notification.getContent();
        if (content != null && content.length() > 160) {
            log.warn("SMS content exceeds 160 characters , so it will be limited to this number");
            content = content.substring(0, 157) ; //We reduced it to 160 characters
        }

        log.info("SMS sending record: Number: {}, Date: {}, Content: {}",
                notification.getId(),
                java.time.LocalDateTime.now(),
                content);

        // to keep the record of the notification in the database we will update the status to SENT
        notification.setStatus("SENT");
        notificationRepositoryAdapter.save(notification);

    }
}
