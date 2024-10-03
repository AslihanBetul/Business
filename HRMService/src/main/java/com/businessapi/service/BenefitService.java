package com.businessapi.service;


import com.businessapi.dto.request.BenefitSaveRequestDTO;
import com.businessapi.dto.request.BenefitUpdateRequestDTO;
import com.businessapi.dto.response.BenefitResponseDTO;
import com.businessapi.entity.Benefit;
import com.businessapi.entity.Employee;
import com.businessapi.exception.HRMException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.BenefitRepository;
import com.businessapi.repository.EmployeeRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitService {
    private final BenefitRepository benefitRepository;
    private final EmployeeRepository employeeRepository;

    public Boolean save(BenefitSaveRequestDTO dto) {
        Benefit benefit=Benefit.builder()
                .employeeId(dto.employeeId())
                .type(dto.type())
                .amount(dto.amount())
                .endDate(dto.endDate())
                .startDate(dto.startDate())
                .status(EStatus.ACTIVE)
                .build();
        benefitRepository.save(benefit);
        return true;
    }

    public Boolean update(BenefitUpdateRequestDTO dto) {
        Benefit benefit = benefitRepository.findById(dto.id()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_BENEFIT));
        benefit.setEmployeeId(dto.employeeId()!=null ? dto.employeeId():benefit.getEmployeeId());
        benefit.setType(dto.type()!=null ? dto.type():benefit.getType());
        benefit.setAmount(dto.amount()!=null ? dto.amount():benefit.getAmount());
        benefit.setEndDate(dto.endDate()!=null ? dto.endDate():benefit.getEndDate());
        benefit.setStartDate(dto.startDate()!=null ? dto.startDate():benefit.getStartDate());
        benefitRepository.save(benefit);
        return true;
    }

    public BenefitResponseDTO findById(Long id) {
        Benefit benefit = benefitRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_BENEFIT));
        return BenefitResponseDTO.builder()
                .amount(benefit.getAmount())
                .endDate(benefit.getEndDate())
                .employeeId(benefit.getEmployeeId())
                .startDate(benefit.getStartDate())
                .type(benefit.getType())
                .build();
    }

    public List<BenefitResponseDTO> findAll() {
        List<Benefit> benefits = benefitRepository.findAll();
        List<BenefitResponseDTO> benefitResponseDTOList=new ArrayList<>();
        benefits.forEach(benefit ->{
                Employee employee = employeeRepository.findById(benefit.getEmployeeId()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_EMPLOYEE));
                benefitResponseDTOList.add(BenefitResponseDTO.builder()
                        .firstName(employee.getFirstName())
                        .lastName(employee.getLastName())
                       .amount(benefit.getAmount())
                       .endDate(benefit.getEndDate())
                       .employeeId(benefit.getEmployeeId())
                       .startDate(benefit.getStartDate())
                       .type(benefit.getType())
                       .build());}
        );
        return benefitResponseDTOList;

    }

    public Boolean delete(Long id) {
        Benefit benefit = benefitRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_BENEFIT));
        benefit.setStatus(EStatus.PASSIVE);
        benefitRepository.save(benefit);
        return true;
    }
}
