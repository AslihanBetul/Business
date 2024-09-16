package com.businessapi.dto.request;

import java.time.LocalDate;

public record PayrollUpdateRequestDTO(
        Long id,
        Long employeeId,
        LocalDate salaryDate,
        Double grossSalary,
        Double deductions,
        Double netSalary) {
}
