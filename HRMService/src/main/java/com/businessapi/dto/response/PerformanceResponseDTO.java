package com.businessapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record PerformanceResponseDTO(
        Long employeeId,
        LocalDate date,
        Integer score,
        String feedback) {
}
