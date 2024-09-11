package com.businessapi.repositories;


import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findAllByNameContainingIgnoreCase(String s, PageRequest of);
    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status AND p.name ILIKE %:name%")
    List<Product> findAllByMinimumStockLevelAndStatusAndNameContainingIgnoreCase(
            EStatus status, String name, PageRequest of);


}
