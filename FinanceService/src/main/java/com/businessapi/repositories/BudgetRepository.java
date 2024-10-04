package com.businessapi.repositories;

import com.businessapi.entity.Budget;
import com.businessapi.entity.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Page<Budget> findAllByStatusNot(EStatus status, Pageable pageable);

    @Query("SELECT b FROM Budget b WHERE b.department LIKE %:department% AND b.status != :status")
    Page<Budget> findByDepartmentContainingIgnoreCaseAndStatusNot(@Param("department") String department, @Param("status") EStatus status, Pageable pageable);

    Budget findByDepartment (String department);
}

