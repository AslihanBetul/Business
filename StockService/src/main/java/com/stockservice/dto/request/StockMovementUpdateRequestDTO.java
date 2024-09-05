package com.stockservice.dto.request;

import com.stockservice.entities.enums.EStockMovementType;

public record StockMovementUpdateRequestDTO(
    Long id,
    Long productId,
    Long warehouseId,
    Integer quantity,
    EStockMovementType stockMovementType
)
{
}
