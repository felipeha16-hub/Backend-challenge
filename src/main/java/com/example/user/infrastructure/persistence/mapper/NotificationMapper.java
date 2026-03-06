package com.example.user.infrastructure.persistence.mapper;

import com.example.user.domain.model.Notification;
import com.example.user.infrastructure.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {


    // converts from entity to domain (to findById, findAll)
    Notification toDomain(NotificationEntity entity);

    // converts from domain to entity (to save)
    NotificationEntity toEntity(Notification notification);



}
