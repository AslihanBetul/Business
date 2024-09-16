package com.businessapi.dto.request;

import com.businessapi.entity.enums.EInvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceUpdateRequestDTO(
        Long id,
        Long customerIdOrSupplierId,
        LocalDate invoiceDate,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        EInvoiceStatus invoiceStatus,
        String description
) {
}
