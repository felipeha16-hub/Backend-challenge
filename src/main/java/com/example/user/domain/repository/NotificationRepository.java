package com.example.user.domain.repository;

import com.example.user.domain.model.Notification;


import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface NotificationRepository {

    Notification save(Notification notification);


    Optional<Notification> findById(Long id);

    void deleteById(Long notificationId);

    List<Notification> findAllById(Long id);


}
