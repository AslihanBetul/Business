package com.businessapi.repositories;

import com.businessapi.entities.Order;
import com.businessapi.entities.enums.EOrderType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findAllByProductIdInAndOrderType(List<Long> ids, EOrderType orderType, PageRequest of);
}
