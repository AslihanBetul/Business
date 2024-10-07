package com.businessapi.services;

import com.businessapi.dto.request.EmployeeSaveRequestDto;
import com.businessapi.dto.request.EmployeeUpdateRequestDto;
import com.businessapi.dto.request.ManagerUpdateRequestDto;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entities.Department;
import com.businessapi.entities.Employee;
import com.businessapi.entities.Manager;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.OrganizationManagementServiceException;
import com.businessapi.repositories.EmployeeRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService
{
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final ManagerService managerService;


    public Boolean save(EmployeeSaveRequestDto dto)
    {
        Manager manager = managerService.findByIdAndMemberId(dto.managerId());
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        employeeRepository.save(Employee.builder().email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).manager(manager).name(dto.name()).surname(dto.surname()).department(department).build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Employee employee = employeeRepository.findByIdAndMemberId(id, SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
        employee.setStatus(EStatus.DELETED);
        employeeRepository.save(employee);
        return true;
    }

    public Boolean update(EmployeeUpdateRequestDto dto)
    {
        Employee employee = employeeRepository.findByIdAndMemberId(dto.id(), SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
        Manager manager = managerService.findByIdAndMemberId(dto.managerId());
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        employee.setIdentityNo(dto.identityNo());
        employee.setName(dto.name());
        employee.setSurname(dto.surname());
        employee.setPhoneNo(dto.phoneNo());
        employee.setManager(manager);
        employee.setDepartment(department);
        employeeRepository.save(employee);
        return true;
    }

    public List<Employee> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(PageRequestDTO dto)
    {
        return employeeRepository.findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(dto.searchText(), SessionManager.getMemberIdFromAuthenticatedMember(), EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public Employee findByIdAndMemberId(Long id)
    {
        return employeeRepository.findByIdAndMemberId(id, SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
    }
}
