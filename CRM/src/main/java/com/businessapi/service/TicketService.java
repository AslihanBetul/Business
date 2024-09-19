package com.businessapi.service;

import com.businessapi.dto.request.TicketSaveDTO;
import com.businessapi.dto.request.TicketUpdateDTO;
import com.businessapi.entity.Ticket;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.TicketRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public Boolean save(TicketSaveDTO dto) {
        ticketRepository.save(Ticket.builder()
                .customerId(dto.customerId())
                .subject(dto.subject())
                .description(dto.description())
                .ticketStatus(dto.ticketStatus())
                .priority(dto.priority())
                .createdDate(dto.createdDate())
                .closedDate(dto.closedDate())
                .build());
        return true;
    }

    public Boolean update(TicketUpdateDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        if (ticket != null && ticket.getStatus().equals(EStatus.DELETED) || ticket.getStatus().equals(EStatus.PASSIVE)) {
            ticket.setCustomerId(dto.customerId()!=null?dto.customerId():ticket.getCustomerId());
            ticket.setSubject(dto.subject()!=null?dto.subject():ticket.getSubject());
            ticket.setDescription(dto.description()!=null?dto.description():ticket.getDescription());
            ticket.setTicketStatus(dto.ticketStatus()!=null?dto.ticketStatus():ticket.getTicketStatus());
            ticket.setPriority(dto.priority()!=null?dto.priority():ticket.getPriority());
            ticket.setCreatedDate(dto.createdDate()!=null?dto.createdDate():ticket.getCreatedDate());
            ticket.setClosedDate(dto.closedDate()!=null?dto.closedDate():ticket.getClosedDate());
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public Boolean delete(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        if (ticket != null && ticket.getStatus().equals(EStatus.DELETED) || ticket.getStatus().equals(EStatus.PASSIVE)) {
            ticket.setStatus(EStatus.DELETED);
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}
