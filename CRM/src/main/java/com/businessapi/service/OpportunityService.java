package com.businessapi.service;

import com.businessapi.dto.request.*;
import com.businessapi.entity.Opportunity;
import com.businessapi.entity.Ticket;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.OpportunityRepository;
import com.businessapi.utility.SessionManager;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OpportunityService {
    private final OpportunityRepository opportunityRepository;
    private  CustomerService customerService;

    @Autowired
    private void setService (@Lazy CustomerService customerService){
        this.customerService = customerService;
    }

    public Boolean save(OpportunitySaveDTO dto) {
        opportunityRepository.save(Opportunity.builder()
                .name(dto.name())
                .description(dto.description())
                .value(dto.value())
                .stage(dto.stage())
                .probability(dto.probability())
                .customerId(dto.customerId())
                .memberId(SessionManager.memberId)
                        .status(EStatus.ACTIVE)
                .build());

        return true;
    }

    public void saveForDemoData(OpportunitySaveDemoDTO dto)
    {
//        if (customerService.findById(dto.customerId()).isPresent()) {
//            throw new CustomerServiceException(ErrorType.NOT_FOUNDED_CUSTOMER);
//        }
        opportunityRepository.save(Opportunity.builder().memberId(2L).name(dto.name()).description(dto.description()).value(dto.value()).stage(dto.stage()).probability(dto.probability()).customerId(dto.customerId()).status(EStatus.ACTIVE).build());

    }


    public List<Opportunity> findAll(PageRequestDTO dto) {
        return opportunityRepository.findAllByNameContainingIgnoreCaseAndStatusIsNotAndMemberIdOrderByNameAsc(dto.searchText(), EStatus.DELETED, SessionManager.memberId, PageRequest.of(dto.page(), dto.size()));

    }

    public Boolean update(OpportunityUpdateDTO dto) {
        Opportunity opportunity = opportunityRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        if (opportunity != null && opportunity.getStatus().equals(EStatus.DELETED) || opportunity.getStatus().equals(EStatus.PASSIVE)) {
            opportunity.setName(dto.name() != null ? dto.name() : opportunity.getName());
            opportunity.setDescription(dto.description() != null ? dto.description() : opportunity.getDescription());
            opportunity.setValue(dto.value() != null ? dto.value() : opportunity.getValue());
            opportunity.setStage(dto.stage() != null ? dto.stage() : opportunity.getStage());
            opportunity.setProbability(dto.probability() != null ? dto.probability() : opportunity.getProbability());
            opportunity.setCustomerId(dto.customerId() != null ? dto.customerId() : opportunity.getCustomerId());
            opportunityRepository.save(opportunity);
            return true;
        } else {
            return false;
        }
    }

    public Boolean delete(Long id) {
        Opportunity opportunity = opportunityRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        if (opportunity != null && opportunity.getStatus().equals(EStatus.DELETED) || opportunity.getStatus().equals(EStatus.PASSIVE)) {
            opportunity.setStatus(EStatus.DELETED);
            opportunityRepository.save(opportunity);
            return true;
        } else {
            return false;
        }

    }

    public Optional<Opportunity> findById(Long id) {
        return opportunityRepository.findById(id);
    }
}
