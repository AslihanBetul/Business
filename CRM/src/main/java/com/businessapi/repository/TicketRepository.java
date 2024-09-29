package com.businessapi.repository;

import com.businessapi.entity.Ticket;
import com.businessapi.utility.enums.EStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllBySubjectContainingIgnoreCaseAndStatusAndMemberIdOrderBySubjectAsc(String s, EStatus eStatus, Long memberId, PageRequest of);
}
