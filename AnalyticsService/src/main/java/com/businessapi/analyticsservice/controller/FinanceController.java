package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.entity.financeService.entity.Expense;
import com.businessapi.analyticsservice.entity.financeService.entity.FinancialReport;
import com.businessapi.analyticsservice.entity.financeService.entity.Invoice;
import com.businessapi.analyticsservice.entity.financeService.entity.Tax;
import com.businessapi.analyticsservice.entity.financeService.enums.EFinancialReportType;
import com.businessapi.analyticsservice.entity.financeService.enums.EInvoiceStatus;
import com.businessapi.analyticsservice.entity.financeService.enums.ETaxType;
import com.businessapi.analyticsservice.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finances")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    /*
     * Invoice
     */
    @GetMapping("/status-count")
    public ResponseEntity<Map<EInvoiceStatus, Long>> getInvoiceStatusCount() throws Exception {
        String jsonInvoice = financeService.getDataFromDataSource("invoice");
        List<Invoice> invoice= financeService.parseInvoice(jsonInvoice);
        Map<EInvoiceStatus, Long> statusCount = financeService.getInvoiceStatusCount(invoice);
        return ResponseEntity.ok(statusCount);
    }

    /*
     * Tax
     */
    @GetMapping("/tax-filter")
    public ResponseEntity<List<Tax>> getTaxByType(@RequestParam(required = false) ETaxType taxType) throws Exception {
        List<Tax> filteredTaxes = financeService.getTaxesByType(taxType);
        return ResponseEntity.ok(filteredTaxes);
    }

    /*
     * Financial Report
     */
    @GetMapping("/financial-report")
    public ResponseEntity<List<FinancialReport>> getFinancialReports(
            @RequestParam(required = false) EFinancialReportType financialReportType,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Double minIncome,
            @RequestParam(required = false) Double maxIncome,
            @RequestParam(required = false) Double minOutcome,
            @RequestParam(required = false) Double maxOutcome,
            @RequestParam(required = false) Double minProfit,
            @RequestParam(required = false) Double maxProfit) throws Exception {

        List<FinancialReport> filteredReports = financeService.getFinancialReports(
                financialReportType, startDate, endDate, minIncome, maxIncome, minOutcome, maxOutcome, minProfit, maxProfit);
        return ResponseEntity.ok(filteredReports);
    }

    /*
     * Expense
     */
    @GetMapping("/expense")
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) throws Exception {

        List<Expense> filteredExpenses = financeService.getExpenses(startDate, endDate, minAmount, maxAmount);
        return ResponseEntity.ok(filteredExpenses);
    }

}
