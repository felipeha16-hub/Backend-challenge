package com.example.user.domain.services;

import com.example.user.domain.model.Notification;

public interface NotificationSenderService {

    boolean supports(String channel);
    void sendNotification(Notification notification);
}
