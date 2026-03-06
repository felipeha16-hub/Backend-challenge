package com.example.user.domain.exceptions.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {

    // 400 - Bad Request
    USER_ALREADY_EXISTS("User already exist", HttpStatus.BAD_REQUEST),
    //INVALID_USER_DATA("Invalid user data.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Password should have 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_NOTIFICATION_CHANNEL("Invalid notification channel, valid channels : Email, SMS, Push Notification ", HttpStatus.BAD_REQUEST),

    //401
    UNAUTHORIZED("Unauthorized access expired token or invalid token ", HttpStatus.UNAUTHORIZED),
    INCORRECT_CREDENTIALS("incorrect credentials", HttpStatus.UNAUTHORIZED),
    //USER_NOT_FOUND("Incorrect Email", HttpStatus.UNAUTHORIZED),
    // 404 - Not Found

    NOTIFICATION_NOT_FOUND("Notification not found", HttpStatus.NOT_FOUND),


    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
