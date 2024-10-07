package com.businessapi.repositories;

import com.businessapi.entities.Manager;
import com.businessapi.entities.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long>
{


    Optional<Manager> findByIdAndMemberId(Long id, Long memberId);

    List<Manager> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(String s, Long memberIdFromAuthenticatedMember, EStatus eStatus, PageRequest of);
}
