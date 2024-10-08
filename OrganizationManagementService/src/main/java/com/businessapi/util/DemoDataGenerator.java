package com.businessapi.util;


import com.businessapi.dto.request.DepartmentSaveRequestDto;
import com.businessapi.dto.request.EmployeeSaveRequestDto;
import com.businessapi.dto.request.ManagerSaveRequestDto;
import com.businessapi.services.DepartmentService;
import com.businessapi.services.EmployeeService;
import com.businessapi.services.ManagerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator
{
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final ManagerService managerService;

    @PostConstruct
    public void generateDemoData()
    {
        departmentDemoData();
        managerDemoData();
        employeeDemoData();



    }

    private void departmentDemoData()
    {
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Human Resources"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Sales"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("IT"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Marketing"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Accounting"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Technical Office"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Research and Development"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Customer Service"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Quality Assurance"));
        departmentService.saveForDemoData(new DepartmentSaveRequestDto("Logistics"));

    }

    private void employeeDemoData()
    {
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(1L, 1L, "12345677442", "05312345678", "Deniz", "Gumus", "deniz@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(1L, 1L, "12345677443", "05322345678", "Ahmet", "Yilmaz", "ahmet@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(1L, 2L, "12345677444", "05332345678", "Ayse", "Kaya", "ayse@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(4L, 2L, "12345677445", "05342345678", "Mehmet", "Demir", "mehmet@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(5L, 5L, "12345677446", "05352345678", "Emine", "Sari", "emine@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(6L, 5L, "12345677447", "05362345678", "Fatma", "Celik", "fatma@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(7L, 4L, "12345677448", "05372345678", "Mustafa", "Kara", "mustafa@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(8L, 4L, "12345677449", "05382345678", "Seda", "Gok", "seda@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(9L, 5L, "12345677450", "05392345678", "Huseyin", "Yildiz", "huseyin@gmail.com"));
        employeeService.saveForDemoData(new EmployeeSaveRequestDto(10L, 8L, "12345677451", "05302345678", "Merve", "Aydin", "merve@gmail.com"));

    }

    private void managerDemoData()
    {

        managerService.saveForDemoData(new ManagerSaveRequestDto(1L, "12345678910", "05312345678", "John", "Doe", "jdoe@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(2L, "22345678910", "05322345678", "Jane", "Smith", "jsmith@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(3L, "32345678910", "05332345678", "Alice", "Johnson", "ajohnson@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(4L, "42345678910", "05342345678", "Bob", "Williams", "bwilliams@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(5L, "52345678910", "05352345678", "Charlie", "Brown", "cbrown@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(6L, "62345678910", "05362345678", "David", "Jones", "djones@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(7L, "72345678910", "05372345678", "Emily", "Davis", "edavis@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(8L, "82345678910", "05382345678", "Frank", "Miller", "fmiller@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(9L, "92345678910", "05392345678", "Grace", "Wilson", "gwilson@gmail.com"));
        managerService.saveForDemoData(new ManagerSaveRequestDto(10L, "02345678910", "05302345678", "Henry", "Moore", "hmoore@gmail.com"));


    }


}
