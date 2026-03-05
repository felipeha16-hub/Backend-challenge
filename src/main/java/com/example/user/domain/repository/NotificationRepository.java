package com.example.user.domain.repository;

import com.example.user.domain.model.Notification;


import java.util.Optional;


public interface NotificationRepository {

    Notification save(Notification notification);


    Optional<Notification> findById(Long id);
}
