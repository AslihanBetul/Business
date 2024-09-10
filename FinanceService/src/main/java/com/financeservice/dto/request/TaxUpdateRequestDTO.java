package com.financeservice.dto.request;

import com.financeservice.entity.enums.ETaxType;

import java.math.BigDecimal;

public record TaxUpdateRequestDTO(
        Long id,
        ETaxType taxType,
        BigDecimal taxRate,
        String description
) {
}
