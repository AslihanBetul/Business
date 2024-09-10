package com.berkhayta.analyticsservice.service;

import com.berkhayta.analyticsservice.dto.request.DataSourceRequestDto;
import com.berkhayta.analyticsservice.dto.response.DataSourceResponseDto;
import com.berkhayta.analyticsservice.entity.DataSource;
import com.berkhayta.analyticsservice.exception.AnalyticsServiceAppException;
import com.berkhayta.analyticsservice.exception.ErrorType;
import com.berkhayta.analyticsservice.mapper.DataSourceMapper;
import com.berkhayta.analyticsservice.repository.DataSourceRepository;
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

    public void fetchDataAndSave() {
        String url = "http://localhost:9099/dev/v1/product/find-all";

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("searchText", "");
        requestBody.put("page", 0);
        requestBody.put("size", 10);

        // Header oluşturulabilir
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonData = response.getBody();  // Tam JSON verisini alıyoruz
            saveDataSource("Product", jsonData);  // Veriyi kaydetmek için metodu çağırıyoruz
//            System.out.println("Veri kaydedildi: " + jsonData);
        } else {
            throw new RuntimeException("Data fetch failed with status: " + response.getStatusCode());
        }
    }

    private void saveDataSource(String serviceType, String data) {
        DataSource dataSource = DataSource.builder()
                .serviceType(serviceType)
                .data(data)  // Gelen JSON veriyi burada saklıyoruz
                .build();

        dataSourceRepository.save(dataSource);  // Veritabanına kaydediyoruz
//        System.out.println("Kaydedilecek veri: " + data);
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
