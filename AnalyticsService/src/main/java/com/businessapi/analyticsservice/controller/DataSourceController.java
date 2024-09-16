package com.businessapi.analyticsservice.controller;

import com.businessapi.analyticsservice.dto.request.DataSourceRequestDto;
import com.businessapi.analyticsservice.dto.response.DataSourceResponseDto;
import com.businessapi.analyticsservice.service.DataSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dataSources")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @PostMapping("/fetch-and-save/{serviceType}")
    public ResponseEntity<Void> fetchDataAndSave(@PathVariable String serviceType) {
        dataSourceService.fetchDataAndSave(serviceType);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<DataSourceResponseDto> createDataSource(@RequestBody DataSourceRequestDto dataSourceRequestDto) {
        return ResponseEntity.ok(dataSourceService.createDataSource(dataSourceRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<DataSourceResponseDto>> getAllDataSources() {
        return ResponseEntity.ok(dataSourceService.getAllDataSources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataSourceResponseDto> getDataSourceById(@PathVariable Long id) {
        return ResponseEntity.ok(dataSourceService.getDataSourceById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable Long id) {
        dataSourceService.deleteDataSource(id);
        return ResponseEntity.noContent().build();
    }
}
