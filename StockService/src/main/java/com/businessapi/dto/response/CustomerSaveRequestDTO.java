package com.businessapi.dto.response;

public record CustomerSaveRequestDTO(
    String name,
    String surname,
    String email
)
{
}
