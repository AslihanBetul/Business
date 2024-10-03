package com.businessapi.dto.response;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record PayrollResponseDTO(
        Long employeeId,
        String firstName,
        String lastName,
        LocalDate salaryDate,
        Double grossSalary,
        Double deductions,
        Double netSalary) {
}
