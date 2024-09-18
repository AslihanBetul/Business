package com.businessapi.repositories;


import com.businessapi.entities.Order;
import com.businessapi.entities.StockMovement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>
{
    List<StockMovement> findAllByProductIdIn(List<Long> productIdList, PageRequest of);
}
