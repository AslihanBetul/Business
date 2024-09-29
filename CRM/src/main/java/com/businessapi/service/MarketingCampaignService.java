package com.businessapi.service;

import com.businessapi.dto.request.*;
import com.businessapi.entity.Customer;
import com.businessapi.entity.MarketingCampaign;
import com.businessapi.exception.CustomerServiceException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.MarketingCampeignRepository;
import com.businessapi.utility.SessionManager;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarketingCampaignService {
    private final MarketingCampeignRepository marketingCampeignRepository;

    public Boolean save(MarketingCampaignSaveDTO dto) {
        marketingCampeignRepository.save(MarketingCampaign.builder()
                .name(dto.name())
                .description(dto.description())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .budget(dto.budget())
                .memberId(SessionManager.getMemberIdFromAuthenticatedMember())
                .status(EStatus.ACTIVE)
                .build());
        return true;
    }

    public void saveForDemoData(MarketingCampaignSaveDemoDTO dto) {
        marketingCampeignRepository.save(MarketingCampaign.builder().memberId(2L).name(dto.name()).description(dto.description()).startDate(dto.startDate()).endDate(dto.endDate()).budget(dto.budget()).status(EStatus.ACTIVE).build());
    }

    public List<MarketingCampaign> findAll(PageRequestDTO dto) {
        return marketingCampeignRepository.findAllByNameContainingIgnoreCaseAndStatusAndMemberIdOrderByNameAsc(dto.searchText(), EStatus.DELETED, SessionManager.getMemberIdFromAuthenticatedMember(), PageRequest.of(dto.page(), dto.size()));

    }


    public Boolean update(MarketingCampaignUpdateDTO dto) {
        MarketingCampaign marketingCampaign = marketingCampeignRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        marketingCampaign.setName(dto.name() != null ? dto.name() : marketingCampaign.getName());
        marketingCampaign.setDescription(dto.description() != null ? dto.description() : marketingCampaign.getDescription());
        marketingCampaign.setStartDate(dto.startDate() != null ? dto.startDate() : marketingCampaign.getStartDate());
        marketingCampaign.setEndDate(dto.endDate() != null ? dto.endDate() : marketingCampaign.getEndDate());
        marketingCampaign.setBudget(dto.budget() != null ? dto.budget() : marketingCampaign.getBudget());
        marketingCampeignRepository.save(marketingCampaign);
        return true;
    }

    public Boolean delete(Long id) {
        MarketingCampaign marketingCampaign = marketingCampeignRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        marketingCampaign.setStatus(EStatus.DELETED);
        marketingCampeignRepository.save(marketingCampaign);
        return true;
    }
}