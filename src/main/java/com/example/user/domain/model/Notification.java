package com.example.user.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String title;
    private String content;
    private String channel; // e.g., "email", "sms", "push"

}
