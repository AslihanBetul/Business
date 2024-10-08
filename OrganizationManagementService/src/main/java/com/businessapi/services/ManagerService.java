package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.EmailSendModal;
import com.businessapi.RabbitMQ.Model.ExistByEmailModel;
import com.businessapi.RabbitMQ.Model.SaveUserFromOtherServicesModel;
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
import com.businessapi.util.PasswordGenerator;
import com.businessapi.util.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService
{
    private final ManagerRepository managerRepository;
    private final DepartmentService departmentService;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    public Boolean save(ManagerSaveRequestDto dto)
    {
        Boolean isEmailExist = (Boolean) (rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyExistByEmail", ExistByEmailModel.builder().email(dto.email()).build()));
        if (Boolean.TRUE.equals(isEmailExist))
        {
            throw new OrganizationManagementServiceException(ErrorType.EMAIL_ALREADY_EXIST);
        }

        String password = PasswordGenerator.generatePassword();
        //saving supplier as auth and user
        Long authId = (Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveUserFromOtherServices", new SaveUserFromOtherServicesModel(dto.name(), dto.surname(), dto.email(), password, "MEMBER"));
        //sending password to new members
        EmailSendModal emailObject = new EmailSendModal(dto.email(), "Supplier Registration", "You can use your mail (" + dto.email() + ") to login. Your password is: " + password + " You can check your orders in our panel.");
        rabbitTemplate.convertAndSend("businessDirectExchange", "keySendMail", emailObject);

        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        managerRepository.save(Manager.builder().authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).memberId(SessionManager.getMemberIdFromAuthenticatedMember()).name(dto.name()).surname(dto.surname()).department(department).build());
        return true;
    }

    public Boolean saveForDemoData(ManagerSaveRequestDto dto)
    {
        Boolean isEmailExist = (Boolean) (rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keyExistByEmail", ExistByEmailModel.builder().email(dto.email()).build()));
        if (Boolean.TRUE.equals(isEmailExist))
        {
            throw new OrganizationManagementServiceException(ErrorType.EMAIL_ALREADY_EXIST);
        }

        String password = PasswordGenerator.generatePassword();
        //saving supplier as auth and user
        Long authId = (Long) rabbitTemplate.convertSendAndReceive("businessDirectExchange", "keySaveUserFromOtherServices", new SaveUserFromOtherServicesModel(dto.name(), dto.surname(), dto.email(), password, "MEMBER"));
        //sending password to new members
        //EmailSendModal emailObject = new EmailSendModal(dto.email(), "Supplier Registration", "You can use your mail (" + dto.email() + ") to login. Your password is: " + password + " You can check your orders in our panel.");
        //rabbitTemplate.convertAndSend("businessDirectExchange", "keySendMail", emailObject);

        Department department = departmentService.findById(dto.departmentId());
        managerRepository.save(Manager.builder().authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).memberId(2L).name(dto.name()).surname(dto.surname()).department(department).build());
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

    public Manager findById(Long id)
    {
        return managerRepository.findById(id).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.MANAGER_NOT_FOUND));
    }
}
