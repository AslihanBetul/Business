package com.financeservice.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseUpdateRequestDTO(
        Long id,
        LocalDateTime expenseDate,
        BigDecimal amount,
        String description
) {
}