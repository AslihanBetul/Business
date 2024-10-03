package com.businessapi.services;

import com.businessapi.dto.request.IncomeFindByDateRequestDTO;
import com.businessapi.dto.request.IncomeSaveRequestDTO;
import com.businessapi.dto.request.IncomeUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entity.Income;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.exception.FinanceServiceException;
import com.businessapi.repositories.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        incomeRepository.save(income);
        return true;
    }

    public Boolean updateIncome(IncomeUpdateRequestDTO dto) {
        Income income = incomeRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(INCOME_NOT_FOUND));
        income.setSource(dto.source());
        income.setAmount(dto.amount());
        income.setIncomeDate(dto.incomeDate());
        return true;
    }

    public List<Income> findByDate(IncomeFindByDateRequestDTO dto) {
        return incomeRepository.findAllByIncomeDateBetweenAndStatusNot(dto.startDate(), dto.endDate(), EStatus.DELETED);
    }

    public List<Income> findByDateForDeclaration(LocalDate startDate, LocalDate endDate) {
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

    public Income findById(Long id) {
        return incomeRepository.findById(id).orElseThrow(() -> new FinanceServiceException(INCOME_NOT_FOUND));
    }

    public List<Income> findAll(PageRequestDTO dto) {
        String source = dto.searchText();
        if (source != null && !source.isEmpty()) {
            return incomeRepository.findBySourceContainingIgnoreCaseAndStatusNot(source, EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
        }
        return incomeRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }


    public List<BigDecimal> getForMonths(IncomeFindByDateRequestDTO dto) {
        List<Income> incomeList = incomeRepository.findAllByIncomeDateBetweenAndStatusNot(dto.startDate(), dto.endDate(), EStatus.DELETED);

        incomeList.sort((o1, o2) -> o1.getIncomeDate().getMonthValue() - o2.getIncomeDate().getMonthValue());

        List<BigDecimal> incomeAmountsOfMonths = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            incomeAmountsOfMonths.add(BigDecimal.ZERO);
        }

        for (Income income : incomeList) {
            int monthIndex = income.getIncomeDate().getMonthValue() - 1;
            BigDecimal currentAmount = incomeAmountsOfMonths.get(monthIndex);
            incomeAmountsOfMonths.set(monthIndex, currentAmount.add(income.getAmount()));
        }
        System.out.println(incomeAmountsOfMonths);
        return incomeAmountsOfMonths;
    }

    public List<String> getMostSource(IncomeFindByDateRequestDTO dto) {
        List<Income> incomeList = incomeRepository.findAllByIncomeDateBetweenAndStatusNot(dto.startDate(), dto.endDate(), EStatus.DELETED);
        incomeList.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
        List<String> mostToLeastSourceList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mostToLeastSourceList.add(incomeList.get(i).getSource());
        }
        return mostToLeastSourceList;
    }
}
