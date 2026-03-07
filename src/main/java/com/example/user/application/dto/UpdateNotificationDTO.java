package com.example.user.application.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UpdateNotificationDTO", description = "DTO to partially update a notification (PATCH)")
public class UpdateNotificationDTO {


    @NotBlank
    @Schema(name = "title", description = "Title of the notification")
    private String title;

    @Schema(name = "content", description = "Content of the notification")
    private String content;

    @Schema(name = "channel", description = "Channel to send the notification (e.g., email, sms, push)")
    private String channel;


}
