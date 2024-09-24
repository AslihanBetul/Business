package com.businessapi.repositories;


import com.businessapi.entities.Supplier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>
{
    List<Supplier> findAllByNameContainingIgnoreCaseOrderByNameAsc(String s, PageRequest of);

    Optional<Supplier> findByAuthId(Long authId);
}
