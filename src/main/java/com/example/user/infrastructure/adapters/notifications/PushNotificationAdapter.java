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
public class PushNotificationAdapter implements NotificationSenderService {

    private final NotificationRepositoryAdapter notificationRepositoryAdapter;

    @Override
    public boolean supports(String channel) {
        return channel.equals("Push Notification");
    }



    @Override
    public void sendNotification(Notification notification) {

        //token validation

        //simulated of token device In real life it would come from the User
        String deviceToken = "fcm_token_a1b2c3d4e5f6g7h8i9j0";



        // simulate format validation token device
        if (deviceToken == null || !deviceToken.startsWith("fcm_token_")) {
            log.error("Invalid device token format. Cannot send push notification.");
            notification.setStatus("FAILED_INVALID_TOKEN");
            notificationRepositoryAdapter.save(notification);
            return;
            }

        // Construir el payload de la notificación push
        String payload = buildPushPayload(deviceToken, notification);

        log.info("Sending push notification to device: {}", deviceToken.substring(0, 20) + "...");
        log.info("Payload: {}", payload);


        notification.setStatus("SENT");
        notificationRepositoryAdapter.save(notification);
        log.info("Push notification sent successfully");
    }

    private String buildPushPayload(String deviceToken, Notification notification) {

        return """
                {
                  "to": "%s",
                  "notification": {
                    "title": "%s",
                    "body": "%s"
                  },
                  "data": {
                    "id": "%s",
                    "sentAt": "%s"
                  }
                }
                """.formatted(
                deviceToken,
                notification.getTitle(),
                notification.getContent(),
                notification.getId(),
                java.time.LocalDateTime.now()
        );
    }
}
