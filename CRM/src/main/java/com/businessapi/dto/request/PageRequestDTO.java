package com.businessapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record PageRequestDTO(
    String searchText,
    @NotNull
    int page,
    int size
   )
{
}
