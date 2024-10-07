package com.businessapi.services;

import com.businessapi.dto.request.DepartmentUpdateRequestDto;
import com.businessapi.dto.request.ManagerSaveRequestDto;
import com.businessapi.dto.request.ManagerUpdateRequestDto;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.entities.Department;
import com.businessapi.entities.Manager;
import com.businessapi.entities.enums.EStatus;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.OrganizationManagementServiceException;
import com.businessapi.repositories.ManagerRepository;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService
{
    private final ManagerRepository managerRepository;
    private final DepartmentService departmentService;


    public Boolean save(ManagerSaveRequestDto dto)
    {
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        managerRepository.save(Manager.builder().email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).memberId(SessionManager.getMemberIdFromAuthenticatedMember()).name(dto.name()).surname(dto.surname()).department(department).build());
        return true;
    }

    public Boolean delete(Long id)
    {
        Manager manager = managerRepository.findByIdAndMemberId(id, SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.MANAGER_NOT_FOUND));
        manager.setStatus(EStatus.DELETED);
        managerRepository.save(manager);
        return true;
    }

    public Boolean update(ManagerUpdateRequestDto dto)
    {
        Manager manager = managerRepository.findByIdAndMemberId(dto.id(), SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.MANAGER_NOT_FOUND));
        manager.setName(dto.name());
        manager.setSurname(dto.surname());
        manager.setIdentityNo(dto.identityNo());
        manager.setPhoneNo(dto.phoneNo());
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        manager.setDepartment(department);
        managerRepository.save(manager);
        return true;
    }

    public List<Manager> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(PageRequestDTO dto)
    {
        return managerRepository.findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(dto.searchText(), SessionManager.getMemberIdFromAuthenticatedMember(), EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
    }

    public Manager findByIdAndMemberId(Long id)
    {
        return managerRepository.findByIdAndMemberId(id, SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.MANAGER_NOT_FOUND));
    }
}
