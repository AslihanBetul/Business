package com.businessapi.dto.request;

public record OpportunityUpdateDTO(Long id, String name, String description, Double value, String stage, Double probability, Long responsibleUserId) {
}
