package com.businessapi.service;


import com.businessapi.dto.request.PerformanceSaveRequestDTO;
import com.businessapi.dto.request.PerformanceUpdateRequestDTO;
import com.businessapi.dto.response.PerformanceResponseDTO;
import com.businessapi.entity.Performance;
import com.businessapi.exception.ErrorType;
import com.businessapi.exception.HRMException;
import com.businessapi.repository.PerformanceRepository;
import com.businessapi.utility.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;

    public Boolean save(PerformanceSaveRequestDTO dto) {
        Performance performance= Performance.builder()
                .employeeId(dto.employeeId())
                .date(dto.date())
                .score(dto.score())
                .feedback(dto.feedback())
                .status(EStatus.ACTIVE)
                .build();
        performanceRepository.save(performance);
        return true;
    }

    public Boolean update(PerformanceUpdateRequestDTO dto) {
        Performance performance = performanceRepository.findById(dto.id()).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PERFORMANCE));
        performance.setEmployeeId(dto.employeeId()!=null?dto.employeeId():performance.getEmployeeId());
        performance.setDate(dto.date()!=null?dto.date():performance.getDate());
        performance.setScore(dto.score()!=null?dto.score():performance.getScore());
        performance.setFeedback(dto.feedback()!=null?dto.feedback():performance.getFeedback());
        performanceRepository.save(performance);
        return true;
    }

    public PerformanceResponseDTO findById(Long id) {
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PERFORMANCE));
        return PerformanceResponseDTO.builder()
               .employeeId(performance.getEmployeeId())
               .date(performance.getDate())
               .score(performance.getScore())
               .feedback(performance.getFeedback())
               .build();
    }

    public List<PerformanceResponseDTO> findAll() {
        List<Performance> performanceList = performanceRepository.findAll();
        List<PerformanceResponseDTO> performanceResponseDTOList=new ArrayList<>();
        performanceList.forEach(performance ->
                performanceResponseDTOList.add(PerformanceResponseDTO.builder()
                       .employeeId(performance.getEmployeeId())
                       .date(performance.getDate())
                       .score(performance.getScore())
                       .feedback(performance.getFeedback())
                       .build())
        );
        return performanceResponseDTOList;
    }

    public Boolean delete(Long id) {
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new HRMException(ErrorType.NOT_FOUNDED_PERFORMANCE));
        performance.setStatus(EStatus.PASSIVE);
        performanceRepository.save(performance);
        return true;
    }
}
