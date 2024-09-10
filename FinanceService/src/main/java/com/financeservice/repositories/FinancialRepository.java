package com.financeservice.repositories;

import com.financeservice.entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRepository extends JpaRepository<FinancialReport, Long> {
}

