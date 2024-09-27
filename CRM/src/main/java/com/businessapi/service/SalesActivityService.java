package com.businessapi.service;

import com.businessapi.dto.request.*;
import com.businessapi.entity.Opportunity;
import com.businessapi.entity.SalesActivity;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.SalesActivityRepository;
import com.businessapi.utility.SessionManager;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SalesActivityService {
    private final SalesActivityRepository salesActivityRepository;
    private OpportunityService opportunityService;

    @Autowired
    private void setService(@Lazy OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    public Boolean save(SalesActivitySaveDTO dto) {
        SalesActivity salesActivity = SalesActivity.builder()
                .opportunityId(dto.opportunityId())
                .type(dto.type())
                .date(dto.date())
                .notes(dto.notes())
                .status(EStatus.ACTIVE)
                .build();
        salesActivityRepository.save(salesActivity);

        return true;

    }

    public void saveForDemoData(SalesActivitySaveDemoDTO dto) {
//        if (opportunityService.findById(dto.opportunityId()).isEmpty()) {
//            throw new RuntimeException("Opportunity not found");
//        }
        salesActivityRepository.save(SalesActivity.builder()
                .opportunityId(dto.opportunityId())
                .type(dto.type())
                .date(dto.date())
                .notes(dto.notes())
                .memberId(2L)
                .status(EStatus.ACTIVE)
                .build()
        );

    }

    public Boolean update(SalesActivityUpdateDTO dto) {
        SalesActivity salesActivity = salesActivityRepository.findById(dto.id()).orElseThrow(() -> new RuntimeException("Sales activity not found"));
        salesActivity.setOpportunityId(dto.opportunityId() != null ? dto.opportunityId() : salesActivity.getOpportunityId());
        salesActivity.setType(dto.type() != null ? dto.type() : salesActivity.getType());
        salesActivity.setDate(dto.date() != null ? dto.date() : salesActivity.getDate());
        salesActivity.setNotes(dto.notes() != null ? dto.notes() : salesActivity.getNotes());
        salesActivityRepository.save(salesActivity);
        return true;
    }

    public Boolean delete(Long id) {
        SalesActivity salesActivity = salesActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Sales activity not found"));
        salesActivity.setStatus(EStatus.DELETED);
        salesActivityRepository.save(salesActivity);
        return true;
    }

    public List<SalesActivity> findAll(PageRequestDTO dto) {
        return salesActivityRepository.findAllByTypeContainingIgnoreCaseAndStatusIsNotAndMemberIdOrderByTypeAsc(dto.searchText(), EStatus.DELETED, SessionManager.memberId, PageRequest.of(dto.page(), dto.size()));
    }
}
