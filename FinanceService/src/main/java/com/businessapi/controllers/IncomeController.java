package com.businessapi.controllers;

import com.businessapi.dto.request.IncomeSaveRequestDTO;
import com.businessapi.dto.request.IncomeUpdateRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Income;
import com.businessapi.services.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + INCOME)
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping(SAVE)
    @Operation(summary = "Creates new income")
    public ResponseEntity<ResponseDTO<Boolean>> save(IncomeSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(incomeService.saveIncome(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates an existing income")
    public ResponseEntity<ResponseDTO<Boolean>> update(IncomeUpdateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(incomeService.updateIncome(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Deletes an existing income")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(incomeService.deleteIncome(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_DATE)
    @Operation(summary = "Lists all incomes with between the given dates")
    public ResponseEntity<ResponseDTO<List<Income>>> findByDate(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Income>>builder()
                .data(incomeService.findByDate(startDate, endDate))
                .message("Success")
                .code(200)
                .build());
    }

}
