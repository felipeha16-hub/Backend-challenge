package com.example.user.infrastructure.controller;


import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.dto.UpdateNotificationDTO;
import com.example.user.application.mapper.NotificationMapper;
import com.example.user.application.useCases.CreateNotificationUseCase;
import com.example.user.application.useCases.DeleteNotificationUseCase;
import com.example.user.application.useCases.GetNotificationsUseCase;
import com.example.user.application.useCases.UpdateNotificationUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final UpdateNotificationUseCase UpdateNotificationUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final GetNotificationsUseCase getNotificationsUseCase;


    @PostMapping("/{create}")
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody CreateNotificationDTO dto) {

        //getName returns the email of the authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createNotificationUseCase.create(dto, email));

    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<NotificationResponseDTO> updateNotification(@PathVariable Long id,@RequestBody UpdateNotificationDTO dto) {

        NotificationResponseDTO updateNotification = UpdateNotificationUseCase.update(id, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateNotification);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        deleteNotificationUseCase.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {

        List<NotificationResponseDTO> notifications = getNotificationsUseCase.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

}
