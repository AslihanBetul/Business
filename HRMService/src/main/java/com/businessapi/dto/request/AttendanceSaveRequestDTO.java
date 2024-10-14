package com.businessapi.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AttendanceSaveRequestDTO(
        Long employeeId,
        LocalDate date,
        LocalTime checkInTime,
        LocalTime checkOutTime) {
}
