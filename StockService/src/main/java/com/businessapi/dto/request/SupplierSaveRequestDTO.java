package com.businessapi.dto.request;

public record SupplierSaveRequestDTO(
    String name,
    String contactInfo,
    String address,
    String notes)
{
}
