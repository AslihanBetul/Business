package com.bilgeadam.businessapi.dto.request;

import com.bilgeadam.businessapi.entity.enums.EStatus;

public record ProjectSaveRequestDTO (Long id, String name, String description, EStatus status){}

