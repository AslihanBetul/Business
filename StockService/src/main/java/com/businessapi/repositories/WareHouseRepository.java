package com.businessapi.repositories;


import com.businessapi.entities.WareHouse;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WareHouseRepository extends JpaRepository<WareHouse, Long>
{
    List<WareHouse> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(String name, Long memberId, EStatus status, PageRequest pageRequest);
}
