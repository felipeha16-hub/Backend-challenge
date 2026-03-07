package com.example.user.domain.exceptions.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    // 400 - Bad Request
    USER_ALREADY_EXISTS("User already exist", HttpStatus.CONFLICT),
    INVALID_PASSWORD("Password should have 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_NOTIFICATION_CHANNEL("Invalid notification channel, valid channels : Email, SMS, Push Notification ", HttpStatus.BAD_REQUEST),

    //401
    INCORRECT_CREDENTIALS("incorrect credentials", HttpStatus.UNAUTHORIZED),

    // 404 - Not Found

    NOTIFICATION_NOT_FOUND("Notification not found", HttpStatus.NOT_FOUND);



    private final String message;
    private final HttpStatus httpStatus;
}
