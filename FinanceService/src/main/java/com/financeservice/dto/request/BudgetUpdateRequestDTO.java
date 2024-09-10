package com.financeservice.dto.request;

import java.math.BigDecimal;

public record BudgetUpdateRequestDTO(
        Long id,
        String department,
        BigDecimal amount,
        Integer year,
        String description
) {
}
