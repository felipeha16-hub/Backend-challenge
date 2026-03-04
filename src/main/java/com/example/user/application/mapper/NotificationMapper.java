package com.example.user.application.mapper;

import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.domain.model.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationMapper {


    public static NotificationResponseDTO toDto(Notification notification) {

        return  new NotificationResponseDTO(
                notification.getTitle(),
                notification.getContent(),
                notification.getChannel()
        );
    }


    public static Notification toDomain(CreateNotificationDTO dto) {
        return new Notification(null,dto.getTitle(), dto.getContent(),dto.getChannel(),null);
    }


}
