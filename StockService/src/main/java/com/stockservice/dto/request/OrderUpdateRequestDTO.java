package com.stockservice.dto.request;

public record OrderUpdateRequestDTO(
        Long id,
        Integer quantity
)
{
}
