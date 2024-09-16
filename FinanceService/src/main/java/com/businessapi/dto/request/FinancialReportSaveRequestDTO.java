package com.businessapi.dto.request;

import com.businessapi.entity.enums.EFinancialReportType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinancialReportSaveRequestDTO(
        EFinancialReportType financialReportType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalIncome,
        BigDecimal totalOutcome,
        BigDecimal totalProfit
) {
}
