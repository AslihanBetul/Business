package com.financeservice.dto.request;

import java.math.BigDecimal;

public record BudgetSaveRequestDTO(
        String department,
        Integer year,
        BigDecimal amount,
        String description
) {
}