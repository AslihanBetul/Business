package com.businessapi.utility;

import com.businessapi.dto.request.EmployeeSaveRequestDTO;
import com.businessapi.entity.Payroll;
import com.businessapi.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator {
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final BenefitService benefitService;
    private final PayrollService payrollService;
    private final PerformanceService performanceService;

    @PostConstruct
    public void generateDemoData() {
    employeeDemoData();

    }
    private void employeeDemoData() {
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Ahmet", "Yılmaz", "Mühendis", "Yazılım", "ahmet.yilmaz@example.com", "1234567890", LocalDate.of(2020, 1, 15), 60000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Ayşe", "Demir", "Analist", "Finans", "ayse.demir@example.com", "0987654321", LocalDate.of(2019, 5, 20), 55000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Mehmet", "Kara", "Proje Yöneticisi", "Yönetim", "mehmet.kara@example.com", "5432167890", LocalDate.of(2021, 3, 12), 70000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Fatma", "Çelik", "Pazarlama Uzmanı", "Pazarlama", "fatma.celik@example.com", "6789054321", LocalDate.of(2022, 6, 25), 50000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Ali", "Öztürk", "Sistem Mühendisi", "IT", "ali.ozturk@example.com", "3216549870", LocalDate.of(2023, 8, 30), 65000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Zeynep", "Koç", "Yazılım Geliştirici", "Yazılım", "zeynep.koc@example.com", "7890123456", LocalDate.of(2021, 11, 5), 62000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Emre", "Aydın", "Veri Bilimci", "Analiz", "emre.aydin@example.com", "4561237890", LocalDate.of(2020, 4, 18), 72000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Elif", "Sönmez", "İK Uzmanı", "İK", "elif.sonmez@example.com", "1357924680", LocalDate.of(2019, 2, 22), 58000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Mert", "Akman", "Yazılım Test Mühendisi", "Yazılım", "mert.akman@example.com", "2468135790", LocalDate.of(2021, 7, 8), 60000.0));
        employeeService.saveForDemoData(new EmployeeSaveRequestDTO("Selin", "Yurt", "Finans Müdürü", "Finans", "selin.yurt@example.com", "1597534862", LocalDate.of(2022, 9, 15), 75000.0));
    }



}
