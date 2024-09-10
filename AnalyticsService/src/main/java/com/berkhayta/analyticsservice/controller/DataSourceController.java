package com.berkhayta.analyticsservice.controller;

import com.berkhayta.analyticsservice.dto.request.DataSourceRequestDto;
import com.berkhayta.analyticsservice.dto.response.DataSourceResponseDto;
import com.berkhayta.analyticsservice.service.DataSourceService;
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

    @PostMapping("/fetch-and-save")
    public ResponseEntity<Void> fetchDataAndSave() {
        dataSourceService.fetchDataAndSave();
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
