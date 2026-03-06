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
public class EmailNotificationAdapter implements NotificationSenderService {


    private final NotificationRepositoryAdapter notificationRepositoryAdapter;


    @Override
    public boolean supports(String channel) {
        return channel.equals("Email");
    }

    @Override
    public void sendNotification(Notification notification) {

        try {

            String email = notification.getUser().getEmail();

            if (email == null || !email.contains("@")) {
                log.error("Email channel validation failure for ID notification: {}", notification.getId());
                notification.setStatus("FAILED_INVALID_FORMAT");
                notificationRepositoryAdapter.save(notification);
                return;
            }

            String htmlTemplate = """
                    <div style='font-family: Arial;'>
                        <h1>Hi, %s</h1>
                        <p> New notification: <b>%s</b></p>
                        <hr>
                        <footer>Send by Backend Challenge</footer>
                    </div>
                    """.formatted(notification.getUser(), notification.getContent());

            log.info("Template generated successfully {}", htmlTemplate);

            log.info("Email, Recipient: {}", notification.getUser().getEmail());
            notification.setStatus("SENT");

            notificationRepositoryAdapter.save(notification);

        } catch (Exception e) {

            try {

                notification.setStatus("FAILED");
                notificationRepositoryAdapter.save(notification);
            } catch (Exception ex) {
                log.error("Error saving the failure state to the database:{}", ex.getMessage());
            }
        }








    }
}
