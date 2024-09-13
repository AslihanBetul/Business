package com.businessapi.repository;





import com.businessapi.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance,Long > {
}
