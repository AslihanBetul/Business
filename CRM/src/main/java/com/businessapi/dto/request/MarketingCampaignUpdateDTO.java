package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record MarketingCampaignUpdateDTO(Long id,
                                         String name,
                                         String description,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         Double budget) {
}
