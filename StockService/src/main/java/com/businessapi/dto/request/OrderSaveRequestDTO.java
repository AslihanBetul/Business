package com.businessapi.dto.request;

public record OrderSaveRequestDTO(Long customerId,
                                  Long productId,
                                  Integer quantity

                                  )

{
}
