package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record AttendanceUpdateRequestDTO(
        Long id,
        Long employeeId,
        LocalDateTime checkInDateTime,
        LocalDateTime checkOutDateTime) {
}
