package com.businessapi.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record AttendanceResponseDTO(
        Long employeeId,
        LocalDateTime checkInDateTime,
        LocalDateTime checkOutDateTime) {
}
