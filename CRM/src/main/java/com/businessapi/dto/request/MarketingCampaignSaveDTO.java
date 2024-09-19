package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record MarketingCampaignSaveDTO(String name,
                                       String description,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       Double budget) {
}
