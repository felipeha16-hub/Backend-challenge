package com.example.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(name = "CreateNotificationResponseDTO", description = "Response DTO to create a notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;


    @NotBlank
    @Schema(name = "title", description = "Title of the notification")
    private String title;



    @NotBlank
    @Schema(name = "content", description = "Content of the notification")
    private String content;


    @NotBlank
    @Schema(name = "channel", description = "Channel to send the notification (e.g., email, sms, push)")
    private String channel;
}
