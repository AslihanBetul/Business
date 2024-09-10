package com.financeservice.dto.request;

import com.financeservice.entity.enums.EInvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceUpdateRequestDTO(
        Long id,
        Long customerIdOrSupplierId,
        LocalDateTime invoiceDate,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        EInvoiceStatus invoiceStatus,
        String description
) {
}
