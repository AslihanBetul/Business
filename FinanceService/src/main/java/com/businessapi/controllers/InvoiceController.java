package com.businessapi.controllers;

import static com.businessapi.constants.Endpoints.*;

import com.businessapi.dto.request.InvoiceSaveRequestDTO;
import com.businessapi.dto.request.InvoiceUpdateRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Invoice;
import com.businessapi.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT + INVOICE)
@CrossOrigin("*")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping(SAVE)
    @Operation(summary = "Saves new invoice")
    public ResponseEntity<ResponseDTO<Boolean>> save(InvoiceSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(invoiceService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates an existing invoice")
    public ResponseEntity<ResponseDTO<Boolean>> update(InvoiceUpdateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(invoiceService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Deletes an existing invoice")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(invoiceService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Lists all the invoices with respect to the given page and size")
    public ResponseEntity<ResponseDTO<List<Invoice>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<Invoice>>builder()
                .data(invoiceService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds an invoice by its id")
    public ResponseEntity<ResponseDTO<Invoice>> findById(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Invoice>builder()
                .data(invoiceService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }
}
