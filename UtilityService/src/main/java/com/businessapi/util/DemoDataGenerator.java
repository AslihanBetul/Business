package com.businessapi.util;

import com.businessapi.dto.request.BugReportSaveRequestDTO;
import com.businessapi.entities.BugReport;
import com.businessapi.services.BugReportService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class DemoDataGenerator
{

    private final BugReportService bugReportService;
    @PostConstruct
    public void generateDemoData()
    {
        generateBugReportData();
    }

    public void generateBugReportData(){

        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("File upload error", "Users are unable to upload files larger than 5MB."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Login issue", "Users are unable to login with valid credentials."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Page crash on submit", "The application crashes when submitting a form."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("UI misalignment", "Buttons are misaligned on the homepage in mobile view."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Search functionality broken", "Search results are not displaying relevant data."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Notification error", "Users are receiving duplicate notifications."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Slow page load", "The dashboard page takes more than 10 seconds to load."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Missing translation", "Some parts of the UI are not translated to the selected language."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Database connection failure", "The application fails to connect to the database intermittently."));
        bugReportService.saveForDemoData(new BugReportSaveRequestDTO("Session timeout issue", "Users are logged out too frequently due to session timeout issues."));

    }
}
