package com.example.user.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;
    private User user;
    private String title;
    private String content;
    private String channel;
    private String status; // e.g., "sent", "pending", "failed"

}
