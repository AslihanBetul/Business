package com.businessapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseSaveRequestDTO(
        LocalDate expenseDate,
        BigDecimal amount,
        String description
) {
}
