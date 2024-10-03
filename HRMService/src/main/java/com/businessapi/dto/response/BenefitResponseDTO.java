package com.businessapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BenefitResponseDTO(
        Long employeeId,
        String firstName,
        String lastName,
        String type,
        Double amount,
        LocalDate startDate,
        LocalDate endDate) {
}
