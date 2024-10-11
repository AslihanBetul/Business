package com.businessapi.repositories;

import com.businessapi.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findDepartmentByName (String departmentName);
}
