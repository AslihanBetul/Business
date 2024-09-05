package com.stockservice.repositories;


import com.stockservice.entities.StockMovement;
import com.stockservice.entities.Supplier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long>
{
    List<Supplier> findAllByNameContaining(String s, PageRequest of);

}
