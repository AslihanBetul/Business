package com.businessapi.services;

import com.businessapi.dto.request.BudgetSaveRequestDTO;
import com.businessapi.dto.request.BudgetUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entity.Budget;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        String department = dto.searchText();
        if (department != null && !department.isEmpty()) {
            return budgetRepository.findByDepartmentContainingIgnoreCaseAndStatusNot(department, EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
        }
        return budgetRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.BUDGET_NOT_FOUND));
    }
}
