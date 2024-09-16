package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.dto.request.ReportRequestDto;
import com.businessapi.analyticsservice.dto.response.ReportResponseDto;
import com.businessapi.analyticsservice.entity.Report;
import com.businessapi.analyticsservice.exception.AnalyticsServiceAppException;
import com.businessapi.analyticsservice.exception.ErrorType;
import com.businessapi.analyticsservice.mapper.ReportMapper;
import com.businessapi.analyticsservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    public ReportResponseDto createReport(ReportRequestDto reportRequestDto) {
        Report report = reportMapper.toEntity(reportRequestDto);
        report = reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    public List<ReportResponseDto> getAllReports() {
        return reportMapper.toDtoList(reportRepository.findAll());
    }

    public ReportResponseDto getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Report not found for id: " + id));
        return reportMapper.toDto(report);
    }

    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "Report not found for id: " + id);
        }
        reportRepository.deleteById(id);
    }
}
