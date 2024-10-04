package com.businessapi.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventSaveRequestDTO(
        String token,
        String title,
        String description,
        String location,
        @NotNull LocalDateTime startDateTime,
        @NotNull LocalDateTime endDateTime) {
}
