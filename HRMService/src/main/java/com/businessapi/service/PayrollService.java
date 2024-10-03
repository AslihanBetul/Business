package com.businessapi.service;


import com.businessapi.dto.request.PayrollSaveRequestDTO;
import com.businessapi.dto.request.PayrollUpdateRequestDTO;
import com.businessapi.dto.response.PageRequestDTO;

import com.businessapi.dto.response.PayrollResponseDTO;
import com.businessapi.entity.Employee;
import com.businessapi.entity.Payroll;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.HRMException;
import com.businessapi.repository.EmployeeRepository;
import com.businessapi.repository.PayrollRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

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

//    public List<PayrollResponseDTO> findAll(PageRequestDTO dto) {
//        List<Payroll> payrolls = payrollRepository.findAll();
//        List<PayrollResponseDTO> payrollResponseDTOList=new ArrayList<>();
//        payrolls.forEach(payroll ->{
//                Employee employee = employeeRepository.findById(payroll.getEmployeeId()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_EMPLOYEE));
//
//                payrollResponseDTOList.add(PayrollResponseDTO.builder()
//                       .employeeId(payroll.getEmployeeId())
//                        .firstName(employee.getFirstName())
//                        .lastName(employee.getLastName())
//                       .salaryDate(payroll.getSalaryDate())
//                       .grossSalary(payroll.getGrossSalary())
//                       .deductions(payroll.getDeductions())
//                       .netSalary(payroll.getNetSalary())
//                       .build());}
//        );
//        return payrollResponseDTOList;
//
//    }


    public List<PayrollResponseDTO> findAll(PageRequestDTO dto) {
        // Sayfa ve boyut bilgilerini al
        int page = dto.page();
        int size = dto.size();
        String searchText = dto.searchText();

        // Pagination için Pageable oluştur
        Pageable pageable = PageRequest.of(page, size);

        // Tüm payroll'ları al
        List<Payroll> payrolls = payrollRepository.findAll();

        // Arama işlemini gerçekleştirmek için DTO listesi oluştur
        List<PayrollResponseDTO> payrollResponseDTOList = new ArrayList<>();

        // Her bir payroll'u DTO'ya dönüştür
        List<PayrollResponseDTO> finalPayrollResponseDTOList = payrollResponseDTOList;
        payrolls.forEach(payroll -> {
            Employee employee = employeeRepository.findById(payroll.getEmployeeId())
                    .orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_EMPLOYEE));

            PayrollResponseDTO payrollResponseDTO = PayrollResponseDTO.builder()
                    .employeeId(payroll.getEmployeeId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .salaryDate(payroll.getSalaryDate())
                    .grossSalary(payroll.getGrossSalary())
                    .deductions(payroll.getDeductions())
                    .netSalary(payroll.getNetSalary())
                    .build();

            // DTO'yu listeye ekle
            finalPayrollResponseDTOList.add(payrollResponseDTO);
        });

        // Arama metnini kullanarak filtrele
        if (searchText != null && !searchText.isEmpty()) {
            payrollResponseDTOList = payrollResponseDTOList.stream()
                    .filter(payrollDto -> payrollDto.firstName().toLowerCase().contains(searchText.toLowerCase()) ||
                            payrollDto.lastName().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Sayfalamayı uygulamak için sonuçları uygun şekilde böl
        int start = Math.min((int) pageable.getOffset(), payrollResponseDTOList.size());
        int end = Math.min(start + pageable.getPageSize(), payrollResponseDTOList.size());
        return payrollResponseDTOList.subList(start, end);
    }




    public Boolean delete(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PAYROLL));
        payroll.setStatus(EStatus.PASSIVE);
        payrollRepository.save(payroll);
        return true;
    }
}



























