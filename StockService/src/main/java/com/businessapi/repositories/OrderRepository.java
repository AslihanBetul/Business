package com.businessapi.repositories;

import com.businessapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{

}
