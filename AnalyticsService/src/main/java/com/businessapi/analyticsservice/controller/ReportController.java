package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.ReportRequestDto;
import com.businessapi.analyticsservice.dto.response.ReportResponseDto;
import com.businessapi.analyticsservice.dto.response.ResponseDTO;
import com.businessapi.analyticsservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dev/v1/analytics/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ReportResponseDto>> createReport(@RequestBody ReportRequestDto reportRequestDto) {
        ReportResponseDto report = reportService.createReport(reportRequestDto);
        return ResponseEntity.ok(ResponseDTO.<ReportResponseDto>builder()
                .data(report)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ReportResponseDto>>> getAllReports() {
        List<ReportResponseDto> reports = reportService.getAllReports();
        return ResponseEntity.ok(ResponseDTO.<List<ReportResponseDto>>builder()
                .data(reports)
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ReportResponseDto>> getReportById(@PathVariable Long id) {
        ReportResponseDto report = reportService.getReportById(id);
        return ResponseEntity.ok(ResponseDTO.<ReportResponseDto>builder()
                .data(report)
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok(ResponseDTO.<Void>builder()
                .message("Success")
                .code(200)
                .build());
    }

}

