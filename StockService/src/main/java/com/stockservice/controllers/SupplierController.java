package com.stockservice.controllers;

import com.stockservice.dto.request.*;
import com.stockservice.dto.response.ResponseDTO;
import com.stockservice.entities.StockMovement;
import com.stockservice.entities.Supplier;
import com.stockservice.services.StockMovementService;
import com.stockservice.services.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + SUPPLIER)
@RequiredArgsConstructor
public class SupplierController
{
    private final SupplierService supplierService;

    @PostMapping(SAVE)
    @Operation(summary = "Creates new Supplier")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody SupplierSaveRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(supplierService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Soft deletes Supplier")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(supplierService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates Supplier")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody SupplierUpdateRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(supplierService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Finds all Suppliers with respect to pagination")
    public ResponseEntity<ResponseDTO<List<Supplier>>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<Supplier>>builder()
                .data(supplierService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds Supplier by Id")
    public ResponseEntity<ResponseDTO<Supplier>> findById(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Supplier>builder()
                .data(supplierService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }
}
