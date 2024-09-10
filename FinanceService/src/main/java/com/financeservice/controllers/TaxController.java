package com.financeservice.controllers;

import static com.financeservice.constants.Endpoints.*;

import com.financeservice.dto.request.PageRequestDTO;
import com.financeservice.dto.request.TaxSaveRequestDTO;
import com.financeservice.dto.request.TaxUpdateRequestDTO;
import com.financeservice.dto.response.ResponseDTO;
import com.financeservice.entity.Tax;
import com.financeservice.services.TaxService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TAX)
@CrossOrigin("*")
public class TaxController {
    private final TaxService taxService;

    @PostMapping(SAVE)
    @Operation(summary = "Saves new tax")
    public ResponseEntity<ResponseDTO<Boolean>> save(TaxSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(taxService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates an existing tax")
    public ResponseEntity<ResponseDTO<Boolean>> update(TaxUpdateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(taxService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Deletes an existing tax")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(taxService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Lists all the taxes with respect to the given page and size")
    public ResponseEntity<ResponseDTO<List<Tax>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Tax>>builder()
                .data(taxService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds a tax by its id")
    public ResponseEntity<ResponseDTO<Tax>> findById(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Tax>builder()
                .data(taxService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }
}



