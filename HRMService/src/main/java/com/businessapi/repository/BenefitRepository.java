package com.businessapi.repository;


import com.businessapi.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitRepository extends JpaRepository<Benefit,Long > {
}
