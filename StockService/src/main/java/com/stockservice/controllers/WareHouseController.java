package com.stockservice.controllers;

import com.stockservice.dto.request.PageRequestDTO;
import com.stockservice.dto.request.WareHouseSaveRequestDTO;
import com.stockservice.dto.request.WareHouseUpdateRequestDTO;
import com.stockservice.dto.response.ResponseDTO;
import com.stockservice.entities.WareHouse;
import com.stockservice.services.WareHouseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.stockservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + WAREHOUSE)
@RequiredArgsConstructor
@CrossOrigin("*")
public class WareHouseController
{
    private final WareHouseService wareHouseService;


    @PostMapping(SAVE)
    @Operation(summary = "Creates new Ware House")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody WareHouseSaveRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(wareHouseService.save(dto))
                        .message("Success")
                        .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Soft deletes Ware House")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(wareHouseService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates Ware House")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody WareHouseUpdateRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(wareHouseService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Finds all Ware House with respect to pagination")
    public ResponseEntity<ResponseDTO<List<WareHouse>>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<WareHouse>>builder()
                .data(wareHouseService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds Ware House by Id")
    public ResponseEntity<ResponseDTO<WareHouse>> findById(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<WareHouse>builder()
                .data(wareHouseService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }




}
