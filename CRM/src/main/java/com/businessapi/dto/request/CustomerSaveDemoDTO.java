package com.businessapi.dto.request;

public record CustomerSaveDemoDTO(Long authId, Long userId,String firstName, String lastName, String email, String phone, String address) {
}
