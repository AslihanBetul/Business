package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record TicketUpdateDTO(Long customerId,
                              String subject,
                              String description,
                              String ticketStatus,
                              String priority,
                              LocalDateTime createdDate,
                              LocalDateTime closedDate,
                              Long id) {
}
