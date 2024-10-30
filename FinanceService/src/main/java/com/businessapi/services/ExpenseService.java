package com.businessapi.services;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.ExpenseCategoryResponseDTO;
import com.businessapi.dto.response.ExpenseResponseDTO;
import com.businessapi.entity.Budget;
import com.businessapi.entity.Department;
import com.businessapi.entity.Expense;
import com.businessapi.entity.enums.EExpenseCategory;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.ExpenseRepository;
import com.businessapi.util.SessionManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final DepartmentService departmentService;

    @Transactional
    public Boolean save(ExpenseSaveRequestDTO dto) {
        Department department = departmentService.getDepartmentById(dto.departmentId());
        department.getExpenses().size();
        Expense expense = Expense.builder()
                .expenseCategory(dto.expenseCategory())
                .expenseDate(dto.expenseDate())
                .amount(dto.amount())
                .description(dto.description())
                .department(department)
                .memberId(SessionManager.getMemberIdFromAuthenticatedMember())
                .build();

        if (dto.expenseCategory().equals(EExpenseCategory.TAX)) {
            expense.setStatus(EStatus.APPROVED);
        }

        department.getExpenses().add(expense);
        expenseRepository.save(expense);
        return true;
    }

    @Transactional
    public Boolean saveForDemoData(ExpenseSaveRequestDTO dto) {
        Department department = departmentService.getDepartmentById(dto.departmentId());
        department.getExpenses().size();
        Expense expense = Expense.builder()
                .expenseCategory(dto.expenseCategory())
                .expenseDate(dto.expenseDate())
                .amount(dto.amount())
                .description(dto.description())
                .department(department)
                .memberId(2L)
                .build();

        if (dto.expenseCategory().equals(EExpenseCategory.TAX)) {
            expense.setStatus(EStatus.APPROVED);
        }

        department.getExpenses().add(expense);
        expenseRepository.save(expense);
        return true;
    }

    public Boolean update(ExpenseUpdateRequestDTO dto) {
        Expense expense = expenseRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setExpenseDate(dto.expenseDate());
        expense.setAmount(dto.amount());
        expense.setDescription(dto.description());
        expense.setExpenseCategory(dto.expenseCategory());

        expenseRepository.save(expense);
        return true;
    }

    public Boolean delete(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setStatus(EStatus.DELETED);
        expenseRepository.save(expense);
        return true;
    }

    public List<ExpenseResponseDTO> findAll(PageRequestDTO dto) {
        Long memberId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Expense> expenseList = expenseRepository.findAllByMemberIdAndStatusNotAndExpenseCategoryNot(memberId, EStatus.DELETED, EExpenseCategory.TAX, PageRequest.of(dto.page(), dto.size())).getContent();
        List<ExpenseResponseDTO> expenseResponseDTOS = new ArrayList<>();
        for (Expense expense : expenseList) {
            expenseResponseDTOS.add(new ExpenseResponseDTO(expense.getId(), expense.getExpenseCategory(), expense.getExpenseDate(), expense.getAmount(), expense.getDescription(), expense.getDepartment().getName()));
        }
        return expenseResponseDTOS;
    }

    public ExpenseResponseDTO findById(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        return new ExpenseResponseDTO(expense.getId(), expense.getExpenseCategory(), expense.getExpenseDate(), expense.getAmount(), expense.getDescription(), expense.getDepartment().getName());
    }

    public Boolean approve(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setStatus(EStatus.APPROVED);
        expenseRepository.save(expense);
        return true;
    }

    public Boolean reject(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setStatus(EStatus.REJECTED);
        expenseRepository.save(expense);
        return true;
    }

    public List<ExpenseResponseDTO> findByDate(LocalDate startDate, LocalDate endDate) {
        Long memberId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Expense> expenseList = expenseRepository.findAllByMemberIdAndExpenseDateBetweenAndStatusNot(memberId, startDate, endDate, EStatus.DELETED);
        List<ExpenseResponseDTO> expenseResponseDTOS = new ArrayList<>();
        for (Expense expense : expenseList) {
            expenseResponseDTOS.add(new ExpenseResponseDTO(expense.getId(), expense.getExpenseCategory(), expense.getExpenseDate(), expense.getAmount(), expense.getDescription(), expense.getDepartment().getName()));
        }
        return expenseResponseDTOS;
    }

    public BigDecimal calculateTotalExpenseBetweenDates(LocalDate startDate, LocalDate endDate) {
        Long memberId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Expense> allExpenseList = expenseRepository.findAllByExpenseDateBetween(startDate, endDate);
        allExpenseList.removeIf(expense -> !expense.getMemberId().equals(memberId));
        BigDecimal totalExpense = BigDecimal.ZERO;
        for (Expense expense : allExpenseList) {
            totalExpense = totalExpense.add(expense.getAmount());
        }
        return totalExpense;
    }


    public List<ExpenseCategoryResponseDTO> getAllExpenseCategories() {
        List<EExpenseCategory> categories = new ArrayList<>(Arrays.asList(EExpenseCategory.values()));
        categories.removeIf(category -> category.equals(EExpenseCategory.TAX));
        List<ExpenseCategoryResponseDTO> expenseCategoriesWithIdAndName = new ArrayList<>();
        for (long l = 1; l < categories.size(); l++) {
            expenseCategoriesWithIdAndName.add(new ExpenseCategoryResponseDTO(l, categories.get((int) l).name()));
        }
        return expenseCategoriesWithIdAndName;
    }

    public List<BigDecimal> getForMonths(ExpenseFindByDateRequestDTO dto) {
        Long memberId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Expense> expenseList = expenseRepository.findAllByMemberIdAndExpenseDateBetweenAndStatusNot(memberId, dto.startDate(), dto.endDate(), EStatus.DELETED);

        expenseList.sort((o1, o2) -> o1.getExpenseDate().getMonthValue() - o2.getExpenseDate().getMonthValue());

        List<BigDecimal> expenseAmountsOfMonths = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            expenseAmountsOfMonths.add(BigDecimal.ZERO);
        }

        for (Expense expense : expenseList) {
            int monthIndex = expense.getExpenseDate().getMonthValue() - 1;
            BigDecimal currentAmount = expenseAmountsOfMonths.get(monthIndex);
            expenseAmountsOfMonths.set(monthIndex, currentAmount.add(expense.getAmount()));
        }
        System.out.println(expenseAmountsOfMonths);
        return expenseAmountsOfMonths;
    }

    public List<ExpenseCategoryResponseDTO> getMostExpensive(ExpenseFindByDateRequestDTO dto) {
        Long memberId = SessionManager.getMemberIdFromAuthenticatedMember();
        List<Expense> expenseList = expenseRepository.findAllByMemberIdAndExpenseDateBetweenAndStatusNot(memberId, dto.startDate(), dto.endDate(), EStatus.DELETED);
        expenseList.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
        List<ExpenseCategoryResponseDTO> mostExpensiveCategories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            if (mostExpensiveCategories.stream().noneMatch(category -> category.expenseCategory().equals(expenseList.get(finalI).getExpenseCategory().name()))) {
                mostExpensiveCategories.add(new ExpenseCategoryResponseDTO((long) (i + 1), expenseList.get(i).getExpenseCategory().name()));
            } else {
                i++;
            }
        }
        return mostExpensiveCategories;
    }
}