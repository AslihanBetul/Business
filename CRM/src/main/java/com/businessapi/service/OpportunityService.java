package com.businessapi.service;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.CustomerResponseForOpportunityDTO;
import com.businessapi.dto.response.OpportunityDetailsDTO;
import com.businessapi.dto.response.OpportunityResponseDTO;
import com.businessapi.entity.Customer;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpportunityService {
    private final OpportunityRepository opportunityRepository;
    private CustomerService customerService;

    @Autowired
    private void setService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }

    public Boolean save(OpportunitySaveDTO dto) {

        Opportunity opportunity = Opportunity.builder()
                .name(dto.name())
                .description(dto.description())
                .value(dto.value())
                .stage(dto.stage())
                .probability(dto.probability())
                .memberId(SessionManager.getMemberIdFromAuthenticatedMember())
                .status(EStatus.ACTIVE)
                .build();


//        Customer customer = customerService.findById(1L);
//        opportunity.setCustomers(List.of(customer));

        opportunityRepository.save(opportunity);

        return true;
    }

    public Boolean saveCustomerOpportunity(OpportunityForCustomerSaveDTO dto) {


        Opportunity opportunity = opportunityRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        SessionManager.authorizationCheck(opportunity.getMemberId());

        List<Customer> newCustomers = customerService.findAllByIds(dto.customers());

        if (dto.customers() == null || dto.customers().isEmpty()) {
            throw new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR);
        }


        List<Customer> existingCustomers = opportunity.getCustomers();


        if (existingCustomers != null) {
            existingCustomers.addAll(newCustomers);
        } else {
            existingCustomers = newCustomers;
        }

        opportunity.setCustomers(existingCustomers);
        opportunityRepository.save(opportunity);
        return true;
    }


    public void saveForDemoData(OpportunitySaveDemoDTO dto) {
        Customer customer = customerService.findById(dto.customerId());
        opportunityRepository.save(Opportunity.builder().memberId(2L).name(dto.name()).description(dto.description()).value(dto.value()).stage(dto.stage()).probability(dto.probability()).customers(List.of(customer)).status(EStatus.ACTIVE).build());
    }


    public List<Opportunity> findAll(PageRequestDTO dto) {
        return opportunityRepository.findAllByNameContainingIgnoreCaseAndStatusAndMemberIdOrderByNameAsc(dto.searchText(), EStatus.ACTIVE, SessionManager.memberId, PageRequest.of(dto.page(), dto.size()));

    }

    public Boolean update(OpportunityUpdateDTO dto) {
        Opportunity opportunity = opportunityRepository.findById(dto.id()).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        SessionManager.authorizationCheck(opportunity.getMemberId());
        if (opportunity != null) {
            opportunity.setName(dto.name() != null ? dto.name() : opportunity.getName());
            opportunity.setDescription(dto.description() != null ? dto.description() : opportunity.getDescription());
            opportunity.setValue(dto.value() != null ? dto.value() : opportunity.getValue());
            opportunity.setStage(dto.stage() != null ? dto.stage() : opportunity.getStage());
            opportunity.setProbability(dto.probability() != null ? dto.probability() : opportunity.getProbability());
            opportunityRepository.save(opportunity);
            return true;
        } else {
            return false;
        }
    }

    public Boolean delete(Long id) {
        Opportunity opportunity = opportunityRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
        SessionManager.authorizationCheck(opportunity.getMemberId());
        if (opportunity != null && opportunity.getStatus().equals(EStatus.ACTIVE)) {
            opportunity.setStatus(EStatus.DELETED);
            opportunityRepository.save(opportunity);
            return true;
        } else {
            return false;
        }

    }

    public Opportunity findById(Long id) {
        return opportunityRepository.findById(id).orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));
    }

    public List<OpportunityResponseDTO> findAllOpportunities() {
        return customerService.getAllCustomersForOpportunity();

    }

    public OpportunityDetailsDTO getDetails(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID geçersiz.");
        }

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new CustomerServiceException(ErrorType.BAD_REQUEST_ERROR));


        SessionManager.authorizationCheck(opportunity.getMemberId());


        List<Long> customerIds = opportunityRepository.findAllCustomersIdById(id);
        List<Customer> customers = customerService.findAllByIds(customerIds);


        if (opportunity != null && opportunity.getStatus().equals(EStatus.ACTIVE)) {

            List<CustomerDetailsDTO> customerDetails = customers.stream()
                    .map(customer -> CustomerDetailsDTO.builder()
                            .firstName(customer.getFirstName())
                            .lastName(customer.getLastName())
                            .build())
                    .collect(Collectors.toList());


            return OpportunityDetailsDTO.builder()
                    .name(opportunity.getName())
                    .description(opportunity.getDescription())
                    .value(opportunity.getValue())
                    .stage(opportunity.getStage())
                    .probability(opportunity.getProbability())
                    .customers(customerDetails)
                    .build();
        } else {
            return null;
        }
    }

}
