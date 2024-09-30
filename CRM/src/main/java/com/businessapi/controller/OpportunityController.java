package com.businessapi.controller;

import com.businessapi.dto.request.OpportunitySaveDTO;
import com.businessapi.dto.request.OpportunityUpdateDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Customer;
import com.businessapi.entity.Opportunity;
import com.businessapi.service.OpportunityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.EndPoints.*;

@RestController
@RequestMapping(OPPORTUNITY)
@RequiredArgsConstructor
@CrossOrigin("*")
public class OpportunityController {
    private final OpportunityService opportunityService;


    @PostMapping(SAVE)
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody OpportunitySaveDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(opportunityService.save(dto))
                .code(200)
                .message("Opportunity saved successfully")
                .build());
    }

    @PostMapping(FINDALL)
    @Operation(summary = "Find all customers", description = "Find all customers")
    public ResponseEntity<ResponseDTO<List<Opportunity>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<List<Opportunity>>builder()
                .data(opportunityService.findAll(dto))
                .code(200)
                .message("Customers found successfully")
                .build());
    }
    @PutMapping(UPDATE)
    @Operation(summary = "Update opportunity by id",description = "Update opportunity by id")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody OpportunityUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(opportunityService.update(dto))
                .code(200)
                .message("Opportunity updated successfully")
                .build());
    }
    @DeleteMapping(DELETE)
    @Operation(summary = "Delete opportunity",description = "Delete opportunity")
    public ResponseEntity<ResponseDTO<Boolean>> delete(@RequestParam Long id) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(opportunityService.delete(id))
                .code(200)
                .message("Opportunity deleted successfully")
                .build());
    }


}
