package com.stockservice.dto.request;

import java.math.BigDecimal;

public record OrderSaveRequestDTO(Long customerId,
                                  Long productId,
                                  Integer quantity

                                  )

{
}
