package com.businessapi.repositories;

import com.businessapi.entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialRepository extends JpaRepository<FinancialReport, Long> {
}

