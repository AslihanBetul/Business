package com.stockservice.dto.request;

public record SupplierUpdateRequestDTO(
        Long id,
        String name,
        String contactInfo,
        String address,
        String notes)
{
}
