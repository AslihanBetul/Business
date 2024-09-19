package com.businessapi.dto.request;

public record OpportunityUpdateDTO(Long id,Long customerId, String name, String description, Double value, String stage, Double probability, Long responsibleUserId) {
}
