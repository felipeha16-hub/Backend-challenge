package com.example.user.infrastructure.controller;


import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.useCases.CreateNotificationUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody CreateNotificationDTO dto) {

        //getName returns the email of the authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createNotificationUseCase.create(dto, email));

    }


}
