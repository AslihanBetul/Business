package com.businessapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseSaveRequestDTO(
        LocalDateTime expenseDate,
        BigDecimal amount,
        String description
) {
}
