package com.example.user.infrastructure.persistence.adapter;


import com.example.user.domain.model.Notification;

import com.example.user.domain.repository.NotificationRepository;

import com.example.user.infrastructure.persistence.JpaNotificationRepository;
import com.example.user.infrastructure.persistence.JpaUserRepository;
import com.example.user.infrastructure.persistence.entity.NotificationEntity;

import com.example.user.infrastructure.persistence.mapper.NotificationMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final JpaNotificationRepository japaNotificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationRepositoryAdapter(JpaNotificationRepository japaNotificationRepository,NotificationMapper notificationMapper) {
        this.japaNotificationRepository = japaNotificationRepository;
        this.notificationMapper =notificationMapper ;
    }



    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = notificationMapper.toEntity(notification);
        NotificationEntity saved = japaNotificationRepository.save(entity);
        return notificationMapper.toDomain(saved);

    }

    @Override
    public Optional<Notification> findById(Long id) {
        return japaNotificationRepository.findById(id).map(notificationMapper::toDomain);
    }
}

