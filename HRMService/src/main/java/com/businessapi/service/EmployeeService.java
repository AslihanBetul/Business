package com.businessapi.service;


import com.businessapi.dto.request.EmployeeSaveRequestDTO;
import com.businessapi.dto.request.EmployeeUpdateRequestDTO;
import com.businessapi.dto.response.EmployeeResponseDTO;
import com.businessapi.entity.Employee;
import com.businessapi.exception.EmployeeException;
import com.businessapi.exception.ErrorType;
import com.businessapi.repository.EmployeeRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public  Boolean save(EmployeeSaveRequestDTO dto) {
        Employee employee=Employee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phone(dto.phone())
                .email(dto.email())
                .position(dto.position())
                .salary(dto.salary())
                .department(dto.department())
                .hireDate(dto.hireDate())
                .status(EStatus.ACTIVE)
                .build();
        employeeRepository.save(employee);
        return true;
    }

    public Boolean update(EmployeeUpdateRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.id()).orElseThrow(() -> new EmployeeException(ErrorType.NOT_FOUNDED_EMPLOYEE));
        employee.setFirstName(dto.firstName()!=null?dto.firstName(): employee.getFirstName());
        employee.setLastName(dto.lastName()!=null?dto.lastName(): employee.getLastName());
        employee.setPhone(dto.phone()!=null?dto.phone():employee.getPhone());
        employee.setEmail(dto.email()!=null?dto.email():employee.getEmail());
        employee.setPosition(dto.position()!=null?dto.position():employee.getPosition());
        employee.setSalary(dto.salary()!=null?dto.salary():employee.getSalary());
        employee.setDepartment(dto.department()!=null?dto.department():employee.getDepartment());
        employee.setHireDate(dto.hireDate()!=null?dto.hireDate():employee.getHireDate());
        employeeRepository.save(employee);
        return true;

    }

    public EmployeeResponseDTO findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeException(ErrorType.NOT_FOUNDED_EMPLOYEE));
        return EmployeeResponseDTO.builder()
               .firstName(employee.getFirstName())
               .lastName(employee.getLastName())
               .phone(employee.getPhone())
               .email(employee.getEmail())
               .position(employee.getPosition())
               .salary(employee.getSalary())
               .department(employee.getDepartment())
               .hireDate(employee.getHireDate())
               .build();

    }

    public List<EmployeeResponseDTO> findAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeResponseDTO> employeeResponseDTOList= new ArrayList<>();
        employeeList.forEach(employee ->
                employeeResponseDTOList.add(EmployeeResponseDTO.builder()

                       .firstName(employee.getFirstName())
                       .lastName(employee.getLastName())
                       .phone(employee.getPhone())
                       .email(employee.getEmail())
                       .position(employee.getPosition())
                       .salary(employee.getSalary())
                       .department(employee.getDepartment())
                       .hireDate(employee.getHireDate())
                       .build())
        );
        return employeeResponseDTOList;
    }

    public Boolean delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeException(ErrorType.NOT_FOUNDED_EMPLOYEE));
        employee.setStatus(EStatus.PASSIVE);
        employeeRepository.save(employee);
        return true;


    }
}



