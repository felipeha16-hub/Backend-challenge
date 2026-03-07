package com.example.user.infrastructure.controller;


import com.example.user.application.dto.CreateNotificationDTO;
import com.example.user.application.dto.NotificationResponseDTO;
import com.example.user.application.dto.UpdateNotificationDTO;
import com.example.user.application.useCases.CreateNotificationUseCase;
import com.example.user.application.useCases.DeleteNotificationUseCase;
import com.example.user.application.useCases.GetNotificationsUseCase;
import com.example.user.application.useCases.UpdateNotificationUseCase;
import com.example.user.infrastructure.error_management.dto.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Notifications", description = "Notification management for authenticated users")
public class NotificationController {

    private final CreateNotificationUseCase createNotificationUseCase;
    private final UpdateNotificationUseCase UpdateNotificationUseCase;
    private final DeleteNotificationUseCase deleteNotificationUseCase;
    private final GetNotificationsUseCase getNotificationsUseCase;


    @PostMapping("/{create}")
    @Operation(
            summary = "Create notification",
            description = "Creates a notification for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "notification_created",
                                    value = "{\"id\":1,\"title\":\"System maintenance\",\"content\":\"Scheduled maintenance at 10:00 PM\",\"channel\":\"Email\"}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "invalid_channel",
                                    value = "{\"timestamp\":1771947757845,\"status\":400,\"error\":\"Bad Request\",\"message\":\"Invalid notification channel, valid channels : Email, SMS, Push Notification \",\"path\":\"/api/v1/notifications/create\"}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "unauthorized",
                                    value = "{\"timestamp\":1771947757845,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized: Full authentication is required to access this resource\",\"path\":\"/api/v1/notifications/create\"}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "internal_error",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\",\"path\":\"/api/v1/notifications/create\"}"
                            )
                    ))
    })
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody CreateNotificationDTO dto) {

        //getName returns the email of the authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createNotificationUseCase.create(dto, email));

    }


    @PatchMapping("/update/{id}")
    @Operation(
            summary = "Update notification",
            description = "Updates an existing notification by its id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "notification_updated",
                                    value = "{\"id\":1,\"title\":\"System maintenance updated\",\"content\":\"Maintenance moved to 11:00 PM\",\"channel\":\"SMS\"}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "invalid_channel",
                                    value = "{\"timestamp\":1771947757845,\"status\":400,\"error\":\"Bad Request\",\"message\":\"Invalid notification channel, valid channels : Email, SMS, Push Notification \",\"path\":\"/api/v1/notifications/update/1\"}"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "unauthorized",
                                    value = "{\"timestamp\":1771947757845,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized: Full authentication is required to access this resource\",\"path\":\"/api/v1/notifications/update/1\"}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "notification_not_found",
                                    value = "{\"timestamp\":1771947757845,\"status\":404,\"error\":\"Not Found\",\"message\":\"Notification not found\",\"path\":\"/api/v1/notifications/update/999\"}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "internal_error",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\",\"path\":\"/api/v1/notifications/update/1\"}"
                            )
                    ))
    })
    public ResponseEntity<NotificationResponseDTO> updateNotification(@PathVariable Long id,@RequestBody UpdateNotificationDTO dto) {

        NotificationResponseDTO updateNotification = UpdateNotificationUseCase.update(id, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateNotification);

    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete notification",
            description = "Deletes a notification by its id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "unauthorized",
                                    value = "{\"timestamp\":1771947757845,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized: Full authentication is required to access this resource\",\"path\":\"/api/v1/notifications/delete/1\"}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "notification_not_found",
                                    value = "{\"timestamp\":1771947757845,\"status\":404,\"error\":\"Not Found\",\"message\":\"Notification not found\",\"path\":\"/api/v1/notifications/delete/999\"}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "internal_error",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\",\"path\":\"/api/v1/notifications/delete/1\"}"
                            )
                    ))
    })
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        deleteNotificationUseCase.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    @Operation(
            summary = "Get notifications",
            description = "Returns all notifications for the authenticated user context."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NotificationResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "notifications_list",
                                    value = "[{\"id\":1,\"title\":\"System maintenance\",\"content\":\"Scheduled maintenance at 10:00 PM\",\"channel\":\"Email\"},{\"id\":2,\"title\":\"Security alert\",\"content\":\"New login detected\",\"channel\":\"Push Notification\"}]"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "unauthorized",
                                    value = "{\"timestamp\":1771947757845,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized: Full authentication is required to access this resource\",\"path\":\"/api/v1/notifications\"}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "internal_error",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\",\"path\":\"/api/v1/notifications\"}"
                            )
                    ))
    })
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {

        List<NotificationResponseDTO> notifications = getNotificationsUseCase.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

}
