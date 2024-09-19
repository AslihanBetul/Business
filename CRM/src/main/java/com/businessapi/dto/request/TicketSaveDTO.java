package com.businessapi.dto.request;

import java.time.LocalDateTime;

public record TicketSaveDTO( Long customerId,
         String subject,
         String description,
         String ticketStatus,
         String priority,
         LocalDateTime createdDate,
         LocalDateTime closedDate) {
}
