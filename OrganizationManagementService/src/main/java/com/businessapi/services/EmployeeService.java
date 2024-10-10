package com.businessapi.services;

import com.businessapi.RabbitMQ.Model.EmailSendModal;
import com.businessapi.RabbitMQ.Model.ExistByEmailModel;
import com.businessapi.RabbitMQ.Model.SaveUserFromOtherServicesModel;
import com.businessapi.dto.request.EmployeeSaveRequestDto;
import com.businessapi.dto.request.EmployeeUpdateRequestDto;
import com.businessapi.dto.request.ManagerUpdateRequestDto;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.EmployeeResponseDTO;
import com.businessapi.dto.response.OrganizationDataDTO;
import com.businessapi.dto.response.OrganizationNodeDTO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        if (!isValidEmail(dto.email()))
        {
            throw new OrganizationManagementServiceException(ErrorType.INVALID_EMAIL);
        }

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

        Employee manager = employeeRepository.findByIdAndMemberId(dto.managerId(), SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
        Department department = departmentService.findByIdAndMemberId(dto.departmentId());
        employeeRepository.save(Employee.builder().memberId(SessionManager.getMemberIdFromAuthenticatedMember()).authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).manager(manager).name(dto.name()).surname(dto.surname()).department(department).build());


        return true;
    }

    public Boolean save(Employee employee)
    {
        employeeRepository.save(employee);
        return true;
    }

    private boolean isValidEmail(String email)
    {

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Employee saveForDemoData(EmployeeSaveRequestDto dto)
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

        Employee manager = employeeRepository.findById(dto.managerId()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
        Department department = departmentService.findById(dto.departmentId());


        return employeeRepository.save(Employee.builder().memberId(2L).authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).manager(manager).name(dto.name()).surname(dto.surname()).department(department).build());
    }

    public Employee saveForDemoDataOwner(EmployeeSaveRequestDto dto)
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

        return employeeRepository.save(Employee.builder().memberId(2L).authId(authId).email(dto.email()).phoneNo(dto.phoneNo()).identityNo(dto.identityNo()).name(dto.name()).surname(dto.surname()).department(department).build());
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
        //employee.setManager(manager);
        employee.setDepartment(department);
        employeeRepository.save(employee);
        return true;
    }

    public List<EmployeeResponseDTO> findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(PageRequestDTO dto)
    {
        List<Employee> employees = employeeRepository.findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(dto.searchText(), SessionManager.getMemberIdFromAuthenticatedMember(), EStatus.DELETED, PageRequest.of(dto.page(), dto.size()));
        List<EmployeeResponseDTO> employeeResponseDTOS = new ArrayList<>();

        for (Employee employee : employees)
        {
            employeeResponseDTOS.add(new EmployeeResponseDTO(employee.getId(), employee.getManager().getName() + " "+  employee.getManager().getSurname(),employee.getDepartment().getName(),employee.getIdentityNo(), employee.getPhoneNo(), employee.getName(), employee.getSurname() , employee.getEmail()));
        }
        return employeeResponseDTOS;
    }

    public Employee findByIdAndMemberId(Long id)
    {
        return employeeRepository.findByIdAndMemberId(id, SessionManager.getMemberIdFromAuthenticatedMember()).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    public Employee findById(Long id)
    {
        return employeeRepository.findById(id).orElseThrow(() -> new OrganizationManagementServiceException(ErrorType.EMPLOYEE_NOT_FOUND));
    }

    public List<OrganizationNodeDTO> getEmployeeHierarchy() {
        List<Employee> employees = employeeRepository.findAll();  // Tüm çalışanları çekiyoruz //TODO BURAYI MEMBERIDLIYE CEVIRICEZ SONRA
        Map<Long, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, employee -> employee));  // Tüm çalışanları map yapısına koyuyoruz

        List<OrganizationNodeDTO> organizationNodes = new ArrayList<>();

        // En üst düzey yöneticileri bulup hiyerarşik yapı oluşturuyoruz
        employees.stream()
                .filter(employee -> employee.getManager() == null)  // Yöneticisi olmayanlar (örneğin CEO)
                .forEach(employee -> organizationNodes.add(convertToOrganizationNode(employee, employeeMap)));

        return organizationNodes;
    }

    private OrganizationNodeDTO convertToOrganizationNode(Employee employee, Map<Long, Employee> employeeMap) {
        OrganizationNodeDTO nodeDTO = new OrganizationNodeDTO();
        nodeDTO.setType("person");
        nodeDTO.setExpanded(true);  // Varsayılan olarak düğümü genişletiyoruz

        // Data alanını dolduruyoruz
        OrganizationDataDTO dataDTO = new OrganizationDataDTO();
        dataDTO.setId(employee.getId());
        dataDTO.setImage("https://randomuser.me/api/portraits/lego/1.jpg");
        dataDTO.setName(employee.getName() + " " + employee.getSurname());
        dataDTO.setTitle(employee.getDepartment() != null ? employee.getDepartment().getName() : "No Department");
        nodeDTO.setData(dataDTO);

        // Alt çalışanları (subordinates) children olarak ekliyoruz
        List<OrganizationNodeDTO> children = employee.getSubordinates().stream()
                .map(subordinate -> convertToOrganizationNode(subordinate, employeeMap))
                .collect(Collectors.toList());
        nodeDTO.setChildren(children);

        return nodeDTO;
    }

}
