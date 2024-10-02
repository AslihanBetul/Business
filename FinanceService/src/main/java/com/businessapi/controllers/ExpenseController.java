package com.businessapi.controllers;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.ExpenseCategoryResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Expense;
import com.businessapi.entity.Income;
import com.businessapi.entity.enums.EExpenseCategory;
import com.businessapi.services.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT + EXPENSE)
@CrossOrigin("*")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping(SAVE)
    @Operation(summary = "Saves new expense")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody ExpenseSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(expenseService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates an existing expense")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody ExpenseUpdateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(expenseService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Deletes an existing expense")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(expenseService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Lists all expenses with respect to the given page and size")
    public ResponseEntity<ResponseDTO<List<Expense>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Expense>>builder()
                .data(expenseService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds an expense by its id")
    public ResponseEntity<ResponseDTO<Expense>> findById(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Expense>builder()
                .data(expenseService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_CATEGORY)
    @Operation(summary = "Finds expenses related to a specific category")
    public ResponseEntity<ResponseDTO<List<Expense>>> findByCategory(EExpenseCategory expenseCategory) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Expense>>builder()
                .data(expenseService.findByCategory(expenseCategory))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(APPROVE)
    @Operation(summary = "Approves an expense")
    public ResponseEntity<ResponseDTO<Boolean>> approve(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(expenseService.approve(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(REJECT)
    @Operation(summary = "Rejects an expense")
    public ResponseEntity<ResponseDTO<Boolean>> reject(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(expenseService.reject(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_DATE)
    @Operation(summary = "Lists all expenses with between the given dates")
    public ResponseEntity<ResponseDTO<List<Expense>>> findByDate(@RequestBody ExpenseFindByDateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Expense>>builder()
                .data(expenseService.findByDate(dto.startDate(), dto.endDate()))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(CALCULATE)
    @Operation(summary = "Calculates total expenses between the given dates")
    public ResponseEntity<ResponseDTO<BigDecimal>> calculateTotalExpenses(@RequestBody ExpenseFindByDateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<BigDecimal>builder()
                .data(expenseService.calculateTotalExpenseBetweenDates(dto.startDate(), dto.endDate()))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(GET_ALL_CATEGORIES)
    @Operation(summary = "Lists all expense categories")
    public ResponseEntity<ResponseDTO<List<ExpenseCategoryResponseDTO>>> getAllCategories() {
        return ResponseEntity.ok(ResponseDTO
                .<List<ExpenseCategoryResponseDTO>>builder()
                .data(expenseService.getAllExpenseCategories())
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(GET_FOR_MONTHS)
    @Operation(summary = "Lists all expenses for the given months")
    public ResponseEntity<ResponseDTO<List<BigDecimal>>> getForMonths(@RequestBody ExpenseFindByDateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<BigDecimal>>builder()
                .data(expenseService.getForMonths(dto))
                .message("Success")
                .code(200)
                .build());
    }

}
