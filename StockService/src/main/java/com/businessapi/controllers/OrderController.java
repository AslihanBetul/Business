package com.businessapi.controllers;

import static com.businessapi.constants.Endpoints.*;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entities.Order;
import com.businessapi.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ROOT + ORDER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController
{
    private final OrderService orderService;

    @PostMapping(SAVE)
    @Operation(summary = "Creates new Order")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody OrderSaveRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(orderService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Soft deletes Order")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(orderService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }


    @PutMapping(UPDATE)
    @Operation(summary = "Updates Order")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody OrderUpdateRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(orderService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Finds all orders with respect to pagination")
    public ResponseEntity<ResponseDTO<List<Order>>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<Order>>builder()
                .data(orderService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds Order by Id")
    public ResponseEntity<ResponseDTO<Order>> findById(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Order>builder()
                .data(orderService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }
}
