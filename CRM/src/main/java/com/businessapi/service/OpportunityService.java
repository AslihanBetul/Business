package com.businessapi.service;

import com.businessapi.dto.request.OpportunitySaveDTO;
import com.businessapi.dto.request.OpportunityUpdateDTO;
import com.businessapi.entity.Customer;
import com.businessapi.entity.Opportunity;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.OpportunityRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpportunityService {
    private final OpportunityRepository opportunityRepository;

    public Boolean save(OpportunitySaveDTO dto) {
       opportunityRepository.save(Opportunity.builder()
              .name(dto.name())
              .description(dto.description())
              .value(dto.value())
              .stage(dto.stage())
              .probability(dto.probability())
              .customerId(dto.customerId())
              .responsibleUserId(dto.responsibleUserId())
              .build());

       return true;
    }

    public List<Opportunity> findAll() {
        return opportunityRepository.findAll();
    }

    public Boolean update(OpportunityUpdateDTO dto) {
        Opportunity opportunity = opportunityRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        if (opportunity != null && opportunity.getStatus().equals(EStatus.DELETED) || opportunity.getStatus().equals(EStatus.PASSIVE)) {
            opportunity.setName(dto.name()!=null?dto.name():opportunity.getName());
            opportunity.setDescription(dto.description()!=null?dto.description():opportunity.getDescription());
            opportunity.setValue(dto.value()!=null?dto.value():opportunity.getValue());
            opportunity.setStage(dto.stage()!=null?dto.stage():opportunity.getStage());
            opportunity.setProbability(dto.probability()!=null?dto.probability():opportunity.getProbability());
            opportunity.setCustomerId(dto.customerId()!=null?dto.customerId():opportunity.getCustomerId());
            opportunity.setResponsibleUserId(dto.responsibleUserId()!=null?dto.responsibleUserId():opportunity.getResponsibleUserId());
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
}
