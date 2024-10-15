package com.businessapi.controllers;

import com.businessapi.dto.request.DeclarationSaveRequestDTO;
import com.businessapi.dto.request.GenerateDeclarationRequestDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.DeclarationResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Declaration;
import com.businessapi.services.DeclarationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + DECLARATION)
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeclarationController {
    private final DeclarationService declarationService;

    @PostMapping(CREATE_FOR_INCOME_TAX)
    @Operation(summary = "Generates a declaration for income tax")
    public ResponseEntity<ResponseDTO<Declaration>> createForIncomeTax(@RequestBody DeclarationSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Declaration>builder()
                .data(declarationService.createDeclarationForIncomeTax(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(CREATE_FOR_VAT)
    @Operation(summary = "Generates a declaration for VAT")
    public ResponseEntity<ResponseDTO<Declaration>> createForVat(@RequestBody DeclarationSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Declaration>builder()
                .data(declarationService.createDeclarationForVat(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(CREATE_FOR_CORPORATE_TAX)
    @Operation(summary = "Generates a declaration for corporate tax")
    public ResponseEntity<ResponseDTO<Declaration>> createForCorporateTax(@RequestBody DeclarationSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Declaration>builder()
                .data(declarationService.createDeclarationForCorporateTax(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(CREATE)
    @Operation(summary = "Generates a declaration")
    public ResponseEntity<ResponseDTO<BigDecimal>> create(@RequestBody GenerateDeclarationRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<BigDecimal>builder()
                .data(declarationService.createDeclaration(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Lists all declarations with respect to the given page and size")
    public ResponseEntity<ResponseDTO<List<DeclarationResponseDTO>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<DeclarationResponseDTO>>builder()
                .data(declarationService.getAllDeclarations(dto))
                .message("Success")
                .code(200)
                .build());
    }
}
