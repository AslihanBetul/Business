package com.businessapi.analyticsservice.service;

import com.businessapi.analyticsservice.dto.request.DataSourceRequestDto;
import com.businessapi.analyticsservice.dto.response.DataSourceResponseDto;
import com.businessapi.analyticsservice.entity.DataSource;
import com.businessapi.analyticsservice.exception.AnalyticsServiceAppException;
import com.businessapi.analyticsservice.exception.ErrorType;
import com.businessapi.analyticsservice.mapper.DataSourceMapper;
import com.businessapi.analyticsservice.repository.DataSourceRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DataSourceService {

    private final DataSourceRepository dataSourceRepository;
    private final DataSourceMapper dataSourceMapper;
    private RestTemplate restTemplate;

    public DataSourceService(DataSourceRepository dataSourceRepository, DataSourceMapper dataSourceMapper, RestTemplate restTemplate) {
        this.dataSourceRepository = dataSourceRepository;
        this.dataSourceMapper = dataSourceMapper;
        this.restTemplate = restTemplate;
    }

    // Fetch serviceType data from Rest API and save it to database
    public void fetchDataAndSave(String serviceType) {
        String url = getUrlByDataSourceType(serviceType);

        if (url == null) {
            throw new IllegalArgumentException("Invalid service type: " + serviceType);
        }

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("searchText", "");
        requestBody.put("page", 0);
        requestBody.put("size", 100);

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonData = response.getBody();

            dataSourceRepository.deleteByServiceType(serviceType);

            saveDataSource(serviceType, jsonData);
        } else {
            throw new RuntimeException("Data fetch failed with status: " + response.getStatusCode());
        }
    }

    private String getUrlByDataSourceType(String serviceType) {
        switch (serviceType.toLowerCase()) {
            case "product":
                return "http://localhost:9099/dev/v1/product/find-all";
            case "order":
                return "http://localhost:9099/dev/v1/order/find-all";
            case "stock-movement":
                return "http://localhost:9099/dev/v1/stock-movement/find-all";
            case "supplier":
                return "http://localhost:9099/dev/v1/supplier/find-all";
            case "ware-house":
                return "http://localhost:9099/dev/v1/ware-house/find-all";
            default:
                return null;
        }
    }

    private void saveDataSource(String serviceType, String data) {
        DataSource dataSource = DataSource.builder()
                .serviceType(serviceType)
                .data(data)
                .build();

        dataSourceRepository.save(dataSource);
    }

    public DataSourceResponseDto createDataSource(DataSourceRequestDto dataSourceRequestDto) {
        DataSource dataSource = dataSourceMapper.toEntity(dataSourceRequestDto);
        dataSource = dataSourceRepository.save(dataSource);
        return dataSourceMapper.toDto(dataSource);
    }

    public List<DataSourceResponseDto> getAllDataSources() {
        return dataSourceMapper.toDtoList(dataSourceRepository.findAll());
    }

    public DataSourceResponseDto getDataSourceById(Long id) {
        DataSource dataSource = dataSourceRepository.findById(id)
                .orElseThrow(() -> new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "DataSource not found for id: " + id));
        return dataSourceMapper.toDto(dataSource);
    }

    public void deleteDataSource(Long id) {
        if (!dataSourceRepository.existsById(id)) {
            throw new AnalyticsServiceAppException(ErrorType.DATA_NOT_FOUND, "DataSource not found for id: " + id);
        }
        dataSourceRepository.deleteById(id);
    }
}