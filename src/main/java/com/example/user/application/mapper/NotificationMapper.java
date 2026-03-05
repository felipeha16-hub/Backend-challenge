package com.example.user.application.mapper;

import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.dto.UpdateNotificationDTO;
import com.example.user.domain.model.Notification;
import com.example.user.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationMapper {


    public static NotificationResponseDTO toDto(Notification notification) {

        return  new NotificationResponseDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getChannel()
        );
    }


    public static Notification toDomain(CreateNotificationDTO dto) {
        return new Notification(null,dto.getTitle(), dto.getContent(),dto.getChannel(),null);
    }

    public static Notification toDomain(UpdateNotificationDTO dto, Notification existing) {

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getContent() != null) existing.setContent(dto.getContent());
        if (dto.getChannel() != null) existing.setChannel(dto.getChannel());
        return existing;
    }


}
