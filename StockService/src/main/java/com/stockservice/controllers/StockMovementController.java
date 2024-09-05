package com.stockservice.controllers;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.StockMovementSaveDTO;
import com.stockservice.dto.request.StockMovementUpdateRequestDTO;
import com.stockservice.dto.response.ResponseDTO;
import com.stockservice.entities.Product;
import com.stockservice.entities.StockMovement;
import com.stockservice.services.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + STOCKMOVEMENT)
@RequiredArgsConstructor
public class StockMovementController
{
    private final StockMovementService stockMovementService;

    @PostMapping(SAVE)
    @Operation(summary = "Creates new Stock Movement")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody StockMovementSaveDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(stockMovementService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Soft deletes Stock Movement")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(stockMovementService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates Stock Movement")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody StockMovementUpdateRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(stockMovementService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Finds all Stock Movements with respect to pagination")
    public ResponseEntity<ResponseDTO<List<StockMovement>>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<StockMovement>>builder()
                .data(stockMovementService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds Stock Movement by Id")
    public ResponseEntity<ResponseDTO<StockMovement>> findById(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<StockMovement>builder()
                .data(stockMovementService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }
}
