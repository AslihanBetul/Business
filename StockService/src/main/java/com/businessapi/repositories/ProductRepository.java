package com.businessapi.repositories;


import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findAllByNameContainingIgnoreCaseOrderByName(String s, PageRequest of);
    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status AND p.name ILIKE %:name% ORDER BY p.name ASC")
    List<Product> findAllByMinimumStockLevelAndStatusAndNameContainingIgnoreCaseOrderByNameAsc(
            EStatus status, String name, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status")
    List<Product> findAllByMinimumStockLevelAndStatus(
            EStatus status);


}
