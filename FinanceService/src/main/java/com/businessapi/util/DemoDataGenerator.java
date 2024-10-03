package com.businessapi.util;

import com.businessapi.dto.request.BudgetSaveRequestDTO;
import com.businessapi.dto.request.ExpenseSaveRequestDTO;
import com.businessapi.dto.request.IncomeSaveRequestDTO;
import com.businessapi.entity.enums.EExpenseCategory;
import com.businessapi.services.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final BudgetService budgetService;
    private final DeclarationService declarationService;
    private final ExpenseService expenseService;
    private final FinancialReportService financialReportService;
    private final IncomeService incomeService;
    private final InvoiceService invoiceService;
    private final TaxService taxService;

    @PostConstruct
    public void generateDemoData() {
        generateBudgetDemoData();
        generateIncomeDemoData();
        generateExpenseDemoData();
    }

    private void generateBudgetDemoData() {
        budgetService.save(new BudgetSaveRequestDTO("SALES", 2025, new BigDecimal(1000000), "2025 Budget for Sales"));
        budgetService.save(new BudgetSaveRequestDTO("MARKETING", 2025, new BigDecimal(500000), "2025 Budget for Marketing"));
        budgetService.save(new BudgetSaveRequestDTO("HR", 2025, new BigDecimal(200000), "2025 Budget for HR"));
        budgetService.save(new BudgetSaveRequestDTO("IT", 2025, new BigDecimal(300000), "2025 Budget for IT"));
        budgetService.save(new BudgetSaveRequestDTO("FINANCE", 2025, new BigDecimal(400000), "2025 Budget for Finance"));
        budgetService.save(new BudgetSaveRequestDTO("R&D", 2025, new BigDecimal(600000), "2025 Budget for R&D"));
        budgetService.save(new BudgetSaveRequestDTO("CSR", 2025, new BigDecimal(100000), "2025 Budget for CSR"));
    }

    private void generateIncomeDemoData() {
        incomeService.saveIncome(new IncomeSaveRequestDTO("Product Sales", new BigDecimal(120000), LocalDate.parse("2024-01-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Service Fees", new BigDecimal(50000), LocalDate.parse("2024-02-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Subscription Fees", new BigDecimal(80000), LocalDate.parse("2024-03-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Licensing Fees", new BigDecimal(30000), LocalDate.parse("2024-04-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Advertising", new BigDecimal(15000), LocalDate.parse("2024-05-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Commission", new BigDecimal(25000), LocalDate.parse("2024-06-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Sponsorship", new BigDecimal(40000), LocalDate.parse("2024-07-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Franchise Fees", new BigDecimal(60000), LocalDate.parse("2024-08-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Interest Income", new BigDecimal(50000), LocalDate.parse("2024-09-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Rental Income", new BigDecimal(10000), LocalDate.parse("2024-10-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Royalties", new BigDecimal(80000), LocalDate.parse("2024-11-15")));
        incomeService.saveIncome(new IncomeSaveRequestDTO("Partnerships/Collaborations", new BigDecimal(20000), LocalDate.parse("2024-12-15")));
    }

    private void generateExpenseDemoData() {
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.BUSSINESS, LocalDate.parse("2024-01-15"), new BigDecimal(20000), "Monthly rent payment"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.EDUCATION, LocalDate.parse("2024-02-15"), new BigDecimal(5000), "Monthly utility bills"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.INSURANCE, LocalDate.parse("2024-03-15"), new BigDecimal(100000), "Monthly salary payments"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.MARKETING, LocalDate.parse("2024-04-15"), new BigDecimal(20000), "Monthly office supplies"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.OFFICE_SUPPLIES, LocalDate.parse("2024-05-15"), new BigDecimal(30000), "Purchase of new equipment"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.PERSONNEL, LocalDate.parse("2024-06-15"), new BigDecimal(10000), "Monthly maintenance costs"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.TRAVEL, LocalDate.parse("2024-07-15"), new BigDecimal(15000), "Monthly insurance premiums"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.UTILITIES, LocalDate.parse("2024-08-15"), new BigDecimal(50000), "Quarterly tax payments"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.EDUCATION, LocalDate.parse("2024-09-15"), new BigDecimal(30000), "Monthly marketing expenses"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.PERSONNEL, LocalDate.parse("2024-10-15"), new BigDecimal(20000), "Monthly travel expenses"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.OFFICE_SUPPLIES, LocalDate.parse("2024-11-15"), new BigDecimal(10000), "Monthly training costs"));
        expenseService.save(new ExpenseSaveRequestDTO(EExpenseCategory.BUSSINESS, LocalDate.parse("2024-12-15"), new BigDecimal(5000), "Monthly entertainment expenses"));
    }
}
