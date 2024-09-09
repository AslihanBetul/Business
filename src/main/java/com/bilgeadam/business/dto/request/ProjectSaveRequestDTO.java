package com.bilgeadam.business.dto.request;

import com.bilgeadam.business.entity.enums.EStatus;

public record ProjectSaveRequestDTO ( String name, String description, EStatus status){}

