package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record EventUpdateRequestDTO(String token, String id,String title, String description, String location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
}
