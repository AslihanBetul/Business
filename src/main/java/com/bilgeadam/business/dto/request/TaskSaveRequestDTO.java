package com.bilgeadam.business.dto.request;

import com.bilgeadam.business.entity.enums.EPriority;
import com.bilgeadam.business.entity.enums.EStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record TaskSaveRequestDTO(Long projectId, String name, String description, Long assignedUserId, EPriority priority, EStatus status){}




