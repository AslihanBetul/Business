package com.stockservice.dto.request;

import com.stockservice.entities.enums.EStockMovementType;

public record StockMovementSaveDTO(Long productId, Long warehouseId, Integer quantity, EStockMovementType stockMovementType)
{


}
