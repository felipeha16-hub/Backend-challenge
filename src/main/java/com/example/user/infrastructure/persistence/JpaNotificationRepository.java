package com.example.user.infrastructure.persistence;

import com.example.user.infrastructure.persistence.entity.NotificationEntity;
import com.example.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {


    List<NotificationEntity> findByUserId(Long userId);


}
