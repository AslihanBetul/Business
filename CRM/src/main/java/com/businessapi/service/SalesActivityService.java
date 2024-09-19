package com.businessapi.service;

import com.businessapi.dto.request.SalesActivitySaveDTO;
import com.businessapi.dto.request.SalesActivityUpdateDTO;
import com.businessapi.entity.SalesActivity;
import com.businessapi.repository.SalesActivityRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SalesActivityService {
    private final SalesActivityRepository salesActivityRepository;

    public Boolean save(SalesActivitySaveDTO dto) {
        SalesActivity salesActivity = SalesActivity.builder()
                .opportunityId(dto.opportunityId())
                .type(dto.type())
                .date(dto.date())
                .notes(dto.notes())
                .build();
        salesActivityRepository.save(salesActivity);

        return true;

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

    public List<SalesActivity> findAll() {
        return salesActivityRepository.findAll();
    }
}
