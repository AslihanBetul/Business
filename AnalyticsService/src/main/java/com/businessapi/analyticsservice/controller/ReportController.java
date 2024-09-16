package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.ReportRequestDto;
import com.businessapi.analyticsservice.dto.response.ReportResponseDto;
import com.businessapi.analyticsservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto reportRequestDto) {
        return ResponseEntity.ok(reportService.createReport(reportRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}

