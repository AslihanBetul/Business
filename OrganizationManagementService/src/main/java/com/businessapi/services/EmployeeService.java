package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.EmailSendModal;
import com.businessapi.RabbitMQ.Model.ExistByEmailModel;
import com.businessapi.RabbitMQ.Model.SaveUserFromOtherServicesModel;
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
public class EmployeeService
{
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final ManagerService managerService;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    public Boolean save(EmployeeSaveRequestDto dto)
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

        Manager manager = managerService.findByIdAndMemberId(dto.managerId());
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        employeeRepository.save(Employee.builder().memberId(SessionManager.getMemberIdFromAuthenticatedMember()).authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).manager(manager).name(dto.name()).surname(dto.surname()).department(department).build());


        return true;
    }

    public Boolean saveForDemoData(EmployeeSaveRequestDto dto)
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

        Manager manager = managerService.findById(dto.managerId());
        Department department = departmentService.findById(dto.departmentId());
        employeeRepository.save(Employee.builder().memberId(2L).authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).manager(manager).name(dto.name()).surname(dto.surname()).department(department).build());


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

    public Employee findById(Long id)
    {
        return employeeRepository.findById(id).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
    }
}
