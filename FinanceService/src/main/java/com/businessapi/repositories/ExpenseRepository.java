package com.businessapi.repositories;

import com.businessapi.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByExpenseCategory(String category);

    List<Expense> findAllByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
}