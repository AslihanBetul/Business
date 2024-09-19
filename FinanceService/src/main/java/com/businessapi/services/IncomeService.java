package com.businessapi.services;

import com.businessapi.dto.request.IncomeSaveRequestDTO;
import com.businessapi.dto.request.IncomeUpdateRequestDTO;
import com.businessapi.entity.Income;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.businessapi.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public Boolean saveIncome(IncomeSaveRequestDTO dto) {
        incomeRepository.save(
                Income.builder()
                .source(dto.source())
                .amount(dto.amount())
                .incomeDate(dto.incomeDate())
                .build()
        );
        return true;
    }

    public Boolean deleteIncome(Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new FinanceServiceException(INCOME_NOT_FOUND));
        income.setStatus(EStatus.DELETED);
        return true;
    }

    public Boolean updateIncome(IncomeUpdateRequestDTO dto) {
        Income income = incomeRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(INCOME_NOT_FOUND));
        income.setSource(dto.source());
        income.setAmount(dto.amount());
        income.setIncomeDate(dto.incomeDate());
        return true;
    }

    public List<Income> findByDate(LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findAllByIncomeDateBetween(startDate, endDate);
    }

    public BigDecimal calculateTotalIncomeBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Income> incomeList = incomeRepository.findAllByIncomeDateBetween(startDate, endDate);
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Income income : incomeList) {
            totalIncome = totalIncome.add(income.getAmount());
        }
        return totalIncome;
    }
}