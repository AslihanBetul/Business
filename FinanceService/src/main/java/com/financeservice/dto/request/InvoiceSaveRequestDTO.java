package com.financeservice.dto.request;

import com.financeservice.entity.enums.EInvoiceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceSaveRequestDTO(
        Long customerIdOrSupplierId,
        LocalDateTime invoiceDate,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        EInvoiceStatus invoiceStatus,
        String description
) {
}
