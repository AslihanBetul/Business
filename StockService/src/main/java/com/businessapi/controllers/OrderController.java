package com.businessapi.controllers;

import static com.businessapi.constants.Endpoints.*;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.BuyOrderResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.dto.response.SellOrderResponseDTO;
import com.businessapi.entities.Order;
import com.businessapi.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ROOT + ORDER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController
{
    private final OrderService orderService;

    @PostMapping(SAVE_SELL_ORDER)
    @Operation(summary = "Creates new sell Order")
    public ResponseEntity<ResponseDTO<Boolean>> saveSellOrder(@RequestBody SellOrderSaveRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(orderService.saveSellOrder(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(SAVE_BUY_ORDER)
    @Operation(summary = "Creates new buy Order")
    public ResponseEntity<ResponseDTO<Boolean>> saveBuyOrder(@RequestBody BuyOrderSaveRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(orderService.saveBuyOrder(dto))
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

    @PostMapping(FIND_ALL_BUY_ORDERS)
    @Operation(summary = "Finds all buy orders with respect to pagination")
    public ResponseEntity<ResponseDTO<List<BuyOrderResponseDTO>>> findAllBuyOrders(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<BuyOrderResponseDTO>>builder()
                .data(orderService.findAllBuyOrders(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL_SELL_ORDERS)
    @Operation(summary = "Finds all sell orders with respect to pagination")
    public ResponseEntity<ResponseDTO<List<SellOrderResponseDTO>>> findAllSellOrders(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<SellOrderResponseDTO>>builder()
                .data(orderService.findAllSellOrders(dto))
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
