package com.businessapi.repository;

import com.businessapi.entity.SalesActivity;
import com.businessapi.utility.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesActivityRepository extends JpaRepository<SalesActivity, Long> {

    List<SalesActivity> findAllByTypeContainingIgnoreCaseAndStatusIsNotAndMemberIdOrderByTypeAsc(String s, EStatus eStatus, Long memberId, PageRequest of);
}
