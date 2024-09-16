package com.businessapi.dto.response;

import com.businessapi.entities.enums.EOrderType;
import com.businessapi.entities.enums.EStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SellOrderResponseDTO(
    Long id,
    String customerName,
    String productName,
    BigDecimal unitPrice,
    BigDecimal total,
    EOrderType orderType,
    LocalDateTime createdAt,
    EStatus status)
{
}
