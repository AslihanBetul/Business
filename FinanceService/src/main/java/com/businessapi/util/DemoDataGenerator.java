package com.businessapi.util;

import com.businessapi.dto.request.BudgetSaveRequestDTO;
import com.businessapi.dto.request.ExpenseSaveRequestDTO;
import com.businessapi.dto.request.IncomeSaveRequestDTO;
import com.businessapi.dto.request.SaveDepartmentRequestDTO;
import com.businessapi.entity.enums.EBudgetCategory;
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
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final DepartmentService departmentService;


    @PostConstruct
    public void generateDemoData() {
        generateDepartmentDemoData();
        generateBudgetDemoData();
        generateIncomeDemoData();
        generateExpenseDemoData();
        //setSpentAmounts();
    }

    private void generateDepartmentDemoData() {
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("SALES"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("MARKETING"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("HR"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("IT"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("FINANCE"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("R&D"));
        departmentService.saveDepartment(new SaveDepartmentRequestDTO("CSR"));
    }

    private void generateBudgetDemoData() {
        budgetService.save(new BudgetSaveRequestDTO(1L, new BigDecimal(200000), EBudgetCategory.OTHER, "Sales budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(1L, new BigDecimal(300000), EBudgetCategory.MARKETING, "Marketing budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(2L, new BigDecimal(500000), EBudgetCategory.MARKETING, "Marketing budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(3L, new BigDecimal(1000000), EBudgetCategory.PERSONNEL, "HR budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(4L, new BigDecimal(200000), EBudgetCategory.OFFICE_SUPPLIES, "IT budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(5L, new BigDecimal(300000), EBudgetCategory.UTILITIES, "Finance budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(5L, new BigDecimal(300000), EBudgetCategory.EDUCATION, "Finance budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(6L, new BigDecimal(100000), EBudgetCategory.TRAVEL, "R&D budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(7L, new BigDecimal(150000), EBudgetCategory.INSURANCE, "CSR budget for 2024"));
        budgetService.save(new BudgetSaveRequestDTO(7L, new BigDecimal(150000), EBudgetCategory.OTHER, "CSR budget for 2024"));
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
        expenseService.save(new ExpenseSaveRequestDTO(1L, EExpenseCategory.BUSSINESS, LocalDate.parse("2024-01-15"), new BigDecimal(20000), "Monthly rent"));
        expenseService.save(new ExpenseSaveRequestDTO(1L, EExpenseCategory.PERSONNEL, LocalDate.parse("2024-01-15"), new BigDecimal(20000), "Salaries"));
        expenseService.save(new ExpenseSaveRequestDTO(2L, EExpenseCategory.OFFICE_SUPPLIES, LocalDate.parse("2024-02-15"), new BigDecimal(50000), "Marketing materials"));
        expenseService.save(new ExpenseSaveRequestDTO(3L, EExpenseCategory.PERSONNEL, LocalDate.parse("2024-03-15"), new BigDecimal(100000), "Salaries"));
        expenseService.save(new ExpenseSaveRequestDTO(4L, EExpenseCategory.TRAVEL, LocalDate.parse("2024-04-15"), new BigDecimal(20000), "Software licenses"));
        expenseService.save(new ExpenseSaveRequestDTO(5L, EExpenseCategory.UTILITIES, LocalDate.parse("2024-05-15"), new BigDecimal(30000), "Electricity"));
        expenseService.save(new ExpenseSaveRequestDTO(5L, EExpenseCategory.UTILITIES, LocalDate.parse("2024-06-15"), new BigDecimal(30000), "Water"));
        expenseService.save(new ExpenseSaveRequestDTO(6L, EExpenseCategory.EDUCATION, LocalDate.parse("2024-06-15"), new BigDecimal(10000), "Training"));
        expenseService.save(new ExpenseSaveRequestDTO(7L, EExpenseCategory.OTHER, LocalDate.parse("2024-07-15"), new BigDecimal(15000), "Donation"));
    }

//    private void setSpentAmounts() {
//        budgetService.setSpentAmount("SALES", new BigDecimal(20000));
//        budgetService.setSpentAmount("MARKETING", new BigDecimal(505000));
//        budgetService.setSpentAmount("HR", new BigDecimal(100000));
//        budgetService.setSpentAmount("IT", new BigDecimal(20000));
//        budgetService.setSpentAmount("FINANCE", new BigDecimal(30000));
//        budgetService.setSpentAmount("R&D", new BigDecimal(10000));
//        budgetService.setSpentAmount("CSR", new BigDecimal(15000));
//    }
}
