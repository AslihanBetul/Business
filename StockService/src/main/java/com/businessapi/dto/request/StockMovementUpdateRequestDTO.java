package com.businessapi.dto.request;

import com.businessapi.entities.enums.EStockMovementType;

public record StockMovementUpdateRequestDTO(
    Long id,
    Long productId,
    Long warehouseId,
    Integer quantity,
    EStockMovementType stockMovementType
)
{
}
