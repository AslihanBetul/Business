package com.businessapi.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceSaveRequestDTO(
        Long employeeId,
        LocalDateTime checkInDateTime,
        LocalDateTime checkOutDateTime) {
}
