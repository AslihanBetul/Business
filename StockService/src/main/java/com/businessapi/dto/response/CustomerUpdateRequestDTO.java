package com.businessapi.dto.response;

public record CustomerUpdateRequestDTO(
        Long id,
        String name,
        String surname,
        String email
)
{
}
