package com.stockservice.repositories;


import com.stockservice.entities.ProductCategory;
import com.stockservice.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>
{
}
