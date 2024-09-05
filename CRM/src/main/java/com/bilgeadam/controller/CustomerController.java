package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constants.EndPoints.CUSTOMER;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/save")
    @Operation(summary = "Save customer",description = "Save customer")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody CustomerSaveDTO customerSaveDTO) {

        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.save(customerSaveDTO))
                .code(200)
                .message("Customer saved successfully").build());

    }



}
