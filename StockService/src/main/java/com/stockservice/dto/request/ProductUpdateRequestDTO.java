package com.stockservice.dto.request;

import java.math.BigDecimal;

public record ProductUpdateRequestDTO(Long id,
                                      Long productCategoryId,
                                      String name,
                                      String description,
                                      BigDecimal price,
                                      Integer stockCount,
                                      Integer minimumStockLevel)
{
}
