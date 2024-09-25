package com.businessapi.repositories;


import com.businessapi.entities.Product;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findAllByNameContainingIgnoreCaseAndStatusAndMemberIdOrderByName(String s, EStatus status, Long memberId, PageRequest of);
    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status AND p.memberId = :memberId AND p.name ILIKE %:name% ORDER BY p.name ASC")
    List<Product> findAllByMinimumStockLevelAndStatusAndNameContainingIgnoreCaseOrderByNameAsc(
            EStatus status, Long memberId, String name, PageRequest of);

    List<Product> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(String name, Long memberId, EStatus status);

    List<Product> findAllByNameContainingIgnoreCaseAndMemberIdOrderByNameAsc(String name, Long memberId);


    @Query("SELECT p FROM Product p WHERE p.stockCount < p.minimumStockLevel AND p.status = :status")
    List<Product> findAllByMinimumStockLevelAndStatus(EStatus status);
}
