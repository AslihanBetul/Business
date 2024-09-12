package com.businessapi.dto.response;

import lombok.Builder;

@Builder
public record CustomerResponseDTO(String firstName, String lastName, String email, String phone, String address) {
}
