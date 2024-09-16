package com.businessapi.dto.request;

import com.businessapi.entity.enums.EInvoiceStatus;

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
