package com.businessapi.service;


import com.businessapi.dto.request.PayrollSaveRequestDTO;
import com.businessapi.dto.request.PayrollUpdateRequestDTO;
import com.businessapi.dto.response.EmployeeResponseDTO;
import com.businessapi.dto.response.PayrollResponseDTO;
import com.businessapi.entity.Payroll;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.HRMException;
import com.businessapi.repository.PayrollRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;

    public Boolean save(PayrollSaveRequestDTO dto) {
        Payroll payroll=Payroll.builder()
                .employeeId(dto.employeeId())
                .salaryDate(dto.salaryDate())
                .grossSalary(dto.grossSalary())
                .deductions(dto.deductions())
                .netSalary(dto.netSalary())
                .status(EStatus.ACTIVE)
                .build();
        payrollRepository.save(payroll);
        return true;
    }

    public Boolean update(PayrollUpdateRequestDTO dto) {
        Payroll payroll = payrollRepository.findById(dto.id()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PAYROLL));
        payroll.setEmployeeId(dto.employeeId()!=null ? dto.employeeId():payroll.getEmployeeId());
        payroll.setSalaryDate(dto.salaryDate()!=null ? dto.salaryDate():payroll.getSalaryDate());
        payroll.setGrossSalary(dto.grossSalary()!=null ? dto.grossSalary():payroll.getGrossSalary());
        payroll.setDeductions(dto.deductions()!=null ? dto.deductions():payroll.getDeductions());
        payroll.setNetSalary(dto.netSalary()!=null ? dto.netSalary():payroll.getNetSalary());
        payrollRepository.save(payroll);
        return true;

    }

    public PayrollResponseDTO findById(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PAYROLL));
        return PayrollResponseDTO.builder()
                .employeeId(payroll.getEmployeeId())
                .salaryDate(payroll.getSalaryDate())
                .grossSalary(payroll.getGrossSalary())
                .deductions(payroll.getDeductions())
                .netSalary(payroll.getNetSalary())
                .build();
    }

    public List<PayrollResponseDTO> findAll() {
        List<Payroll> payrolls = payrollRepository.findAll();
        List<PayrollResponseDTO> payrollResponseDTOList=new ArrayList<>();
        payrolls.forEach(payroll ->
                payrollResponseDTOList.add(PayrollResponseDTO.builder()
                       .employeeId(payroll.getEmployeeId())
                       .salaryDate(payroll.getSalaryDate())
                       .grossSalary(payroll.getGrossSalary())
                       .deductions(payroll.getDeductions())
                       .netSalary(payroll.getNetSalary())
                       .build())
        );
        return payrollResponseDTOList;

    }

    public Boolean delete(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PAYROLL));
        payroll.setStatus(EStatus.PASSIVE);
        payrollRepository.save(payroll);
        return true;
    }
}



























