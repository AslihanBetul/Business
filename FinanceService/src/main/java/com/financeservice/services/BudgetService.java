package com.financeservice.services;

import com.financeservice.dto.request.BudgetSaveRequestDTO;
import com.financeservice.dto.request.BudgetUpdateRequestDTO;
import com.financeservice.dto.request.PageRequestDTO;
import com.financeservice.entity.Budget;
import com.financeservice.entity.enums.EStatus;
import com.financeservice.exception.ErrorType;
import com.financeservice.exception.FinanceServiceException;
import com.financeservice.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public Boolean save(BudgetSaveRequestDTO dto) {
        Budget budget = Budget.builder()
                .department(dto.department())
                .amount(dto.amount())
                .year(dto.year())
                .description(dto.description())
                .build();

        budgetRepository.save(budget);
        return true;
    }

    public Boolean update(BudgetUpdateRequestDTO dto) {
        Budget budget = budgetRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
        budget.setDepartment(dto.department());
        budget.setAmount(dto.amount());
        budget.setYear(dto.year());
        budget.setDescription(dto.description());

        budgetRepository.save(budget);

        return true;
    }

    public Boolean delete(Long id) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
        budget.setStatus(EStatus.DELETED);

        budgetRepository.save(budget);

        return true;
    }

    public List<Budget> findAll(PageRequestDTO dto) {
        return budgetRepository.findAll(PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
    }
}
