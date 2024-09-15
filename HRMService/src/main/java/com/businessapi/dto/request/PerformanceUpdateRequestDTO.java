package com.businessapi.dto.request;

import java.time.LocalDate;

public record PerformanceUpdateRequestDTO(
        Long id,
        Long employeeId,
        LocalDate date,
        Integer score,
        String feedback) {
}
