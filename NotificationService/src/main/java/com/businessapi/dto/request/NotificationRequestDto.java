package com.businessapi.dto.request;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private Long userId;
    private String title;
    private String message;
}
