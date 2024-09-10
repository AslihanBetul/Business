package com.financeservice.dto.request;

import com.financeservice.entity.enums.ETaxType;

import java.math.BigDecimal;

public record TaxSaveRequestDTO(
        ETaxType taxType,
        BigDecimal taxRate,
        String description
) {
}
