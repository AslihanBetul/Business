package com.businessapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record AttendanceResponseDTO(
        Long employeeId,
        String firstName,
        String lastName,
        LocalDateTime checkInDateTime,
        LocalDateTime checkOutDateTime) {
}
