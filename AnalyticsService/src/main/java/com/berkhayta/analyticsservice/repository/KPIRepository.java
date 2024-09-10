package com.berkhayta.analyticsservice.repository;

import com.berkhayta.analyticsservice.entity.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KPIRepository extends JpaRepository<KPI, Long> {
}
