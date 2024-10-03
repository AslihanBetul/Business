package com.businessapi.services;

import com.businessapi.dto.request.DeclarationSaveRequestDTO;
import com.businessapi.dto.request.ExpenseFindByDateRequestDTO;
import com.businessapi.dto.request.GenerateDeclarationRequestDTO;
import com.businessapi.entity.Declaration;
import com.businessapi.entity.Expense;
import com.businessapi.entity.Income;
import com.businessapi.entity.enums.EStatus;
import com.businessapi.repositories.DeclarationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeclarationService {
    private final DeclarationRepository declarationRepository;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final TaxService taxService;

    public Declaration createDeclarationForIncomeTax(DeclarationSaveRequestDTO dto) {
        BigDecimal taxableIncome = calculateTaxableIncome(dto).get(2);
        BigDecimal totalTax = taxService.calculateIncomeTax(taxableIncome);

        Declaration declaration = Declaration.builder()
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalIncome(calculateTaxableIncome(dto).get(0))
                .totalExpense(calculateTaxableIncome(dto).get(1))
                .totalTax(totalTax)
                .build();

        declarationRepository.save(declaration);
        return declaration;
    }

    public Declaration createDeclarationForVat(DeclarationSaveRequestDTO dto) {
        BigDecimal taxableIncome = calculateTaxableIncome(dto).get(2);
        BigDecimal totalTax = taxService.calculateVat(taxableIncome);

        Declaration declaration = Declaration.builder()
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalIncome(calculateTaxableIncome(dto).get(0))
                .totalExpense(calculateTaxableIncome(dto).get(1))
                .totalTax(totalTax)
                .build();

        declarationRepository.save(declaration);
        return declaration;
    }

    public Declaration createDeclarationForCorporateTax(DeclarationSaveRequestDTO dto) {
        BigDecimal taxableIncome = calculateTaxableIncome(dto).get(2);
        BigDecimal totalTax = taxService.calculateCorporateTax(taxableIncome);

        Declaration declaration = Declaration.builder()
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalIncome(calculateTaxableIncome(dto).get(0))
                .totalExpense(calculateTaxableIncome(dto).get(1))
                .totalTax(totalTax)
                .build();

        declarationRepository.save(declaration);
        return declaration;
    }

    private List<BigDecimal> calculateTaxableIncome(DeclarationSaveRequestDTO dto){
        List<Income> incomeList = incomeService.findByDateForDeclaration(dto.startDate(), dto.endDate());
        List<Expense> expenseList = expenseService.findByDate(dto.startDate(), dto.endDate());
        List<Expense> approvedExpenseList = expenseList.stream().filter(expense -> expense.getStatus().equals(EStatus.APPROVED)).toList();

        BigDecimal totalIncome = incomeList.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = approvedExpenseList.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxableIncome = totalIncome.subtract(totalExpense);

        return List.of(totalIncome, totalExpense, taxableIncome);
    }


    public BigDecimal createDeclaration(GenerateDeclarationRequestDTO dto) {
        return switch (dto.taxType()) {
            case "income" -> taxService.calculateIncomeTax(dto.netIncome());
            case "kdv" -> taxService.calculateVat(dto.netIncome());
            case "corporate" -> taxService.calculateCorporateTax(dto.netIncome());
            default -> throw new IllegalStateException("Invalid Tax Type: " + dto.taxType());
        };
    }
}
