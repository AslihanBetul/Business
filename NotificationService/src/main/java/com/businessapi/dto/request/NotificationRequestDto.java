package com.businessapi.dto.request;

import lombok.Data;

@Data
public class NotificationRequestDto {
    private String userId;
    private String message;
}
