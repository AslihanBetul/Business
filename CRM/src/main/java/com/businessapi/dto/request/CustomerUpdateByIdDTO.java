package com.businessapi.dto.request;

public record CustomerUpdateByIdDTO(Long id, String firstName, String lastName, String email, String phone, String address) {
}
