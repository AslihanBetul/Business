package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.dto.request.KPIRequestDto;
import com.businessapi.analyticsservice.dto.response.KPIResponseDto;
import com.businessapi.analyticsservice.entity.KPI;
import com.businessapi.analyticsservice.exception.AnalyticsServiceAppException;
import com.businessapi.analyticsservice.exception.ErrorType;
import com.businessapi.analyticsservice.mapper.KPIMapper;
import com.businessapi.analyticsservice.repository.KPIRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KPIService {

    private final KPIRepository kpiRepository;
    private final KPIMapper kpiMapper;

    public KPIService(KPIRepository kpiRepository, KPIMapper kpiMapper) {
        this.kpiRepository = kpiRepository;
        this.kpiMapper = kpiMapper;
    }

    public KPIResponseDto createKPI(KPIRequestDto kpiRequestDto) {
        KPI kpi = kpiMapper.toEntity(kpiRequestDto);
        kpi = kpiRepository.save(kpi);
        return kpiMapper.toDto(kpi);
    }

    public List<KPIResponseDto> getAllKPIs() {
        return kpiMapper.toDtoList(kpiRepository.findAll());
    }

    public KPIResponseDto getKPIById(Long id) {
        KPI kpi = kpiRepository.findById(id)
                .orElseThrow(() -> new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "KPI not found"));
        return kpiMapper.toDto(kpi);
    }

    public void deleteKPI(Long id) {
        if (!kpiRepository.existsById(id)) {
            throw new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "KPI not found");
        }
        kpiRepository.deleteById(id);
    }
}
