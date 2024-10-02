package com.businessapi.dto.request;

import java.math.BigDecimal;

public record GenerateDeclarationRequestDTO(
        String taxType,
        BigDecimal netIncome
) {
}
