package com.financeservice.services;

import com.financeservice.dto.request.ExpenseSaveRequestDTO;
import com.financeservice.dto.request.ExpenseUpdateRequestDTO;
import com.financeservice.dto.request.PageRequestDTO;
import com.financeservice.entity.Expense;
import com.financeservice.entity.enums.EStatus;
import com.financeservice.exception.ErrorType;
import com.financeservice.exception.FinanceServiceException;
import com.financeservice.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public Boolean save(ExpenseSaveRequestDTO dto) {
        Expense expense = Expense.builder()
                .expenseDate(dto.expenseDate())
                .amount(dto.amount())
                .description(dto.description())
                .build();

        expenseRepository.save(expense);
        return true;
    }

    public Boolean update(ExpenseUpdateRequestDTO dto) {
        Expense expense = expenseRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setExpenseDate(dto.expenseDate());
        expense.setAmount(dto.amount());
        expense.setDescription(dto.description());

        expenseRepository.save(expense);
        return true;
    }

    public Boolean delete(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
        expense.setStatus(EStatus.DELETED);
        expenseRepository.save(expense);
        return true;
    }

    public List<Expense> findAll(PageRequestDTO dto) {
        return expenseRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.EXPENSE_NOT_FOUND));
    }
}