package com.example.user.infrastructure.adapters;


import com.example.user.domain.model.Notification;
import com.example.user.infrastructure.adapters.notifications.EmailNotificationAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class EmailNotificationAdapterTest {



    @InjectMocks
    private EmailNotificationAdapter emailAdapter;


    @Mock
    private com.example.user.infrastructure.persistence.adapter.NotificationRepositoryAdapter notificationRepositoryAdapter;

    @Test
    @DisplayName("Unit Test - EmailAdapter should handle exceptions and update status")
    void shouldHandleUnexpectedExceptionDuringEmailSending() {
        // Generate of notification
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setContent("Test Content");
        notification.setStatus("PENDING");

        // Simulation of behavior
        // force the save method to throw an exception when called
        doThrow(new RuntimeException("Simulated Mail Server Error"))
                .when(notificationRepositoryAdapter).save(any());

        emailAdapter.sendNotification(notification);

        // verify that the save method was called at least once
        verify(notificationRepositoryAdapter, atLeastOnce()).save(any());

        // verify that the status of the notification is updated to "FAILED" after the exception
        assertThat(notification.getStatus()).isEqualTo("FAILED");
    }
}
