package com.financeservice.entity;


import com.financeservice.entity.enums.EFinancialReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblfinancialreport")
public class FinancialReport extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    EFinancialReportType financialReportType;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalIncome;
    BigDecimal totalOutcome;
    BigDecimal totalProfit;
}
