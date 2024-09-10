package com.berkhayta.analyticsservice.service;

import com.berkhayta.analyticsservice.dto.request.ReportRequestDto;
import com.berkhayta.analyticsservice.dto.response.ReportResponseDto;
import com.berkhayta.analyticsservice.entity.Report;
import com.berkhayta.analyticsservice.exception.AnalyticsServiceAppException;
import com.berkhayta.analyticsservice.exception.ErrorType;
import com.berkhayta.analyticsservice.mapper.ReportMapper;
import com.berkhayta.analyticsservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
