package com.businessapi.dto.request;

import com.businessapi.entities.enums.EStockMovementType;

public record StockMovementSaveDTO(Long productId, Long warehouseId, Integer quantity, EStockMovementType stockMovementType)
{


}
