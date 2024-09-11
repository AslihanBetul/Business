package com.businessapi.repositories;


import com.businessapi.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>
{
}
