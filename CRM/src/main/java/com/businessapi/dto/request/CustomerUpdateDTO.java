package com.businessapi.dto.request;

public record CustomerUpdateDTO(String token, String firstName, String lastName, String email, String phone, String address) {
}
