package com.businessapi.repositories;

import com.businessapi.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByIncomeDateBetween(LocalDate startDate, LocalDate endDate);
}
