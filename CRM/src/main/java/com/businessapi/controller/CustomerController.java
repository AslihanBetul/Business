package com.businessapi.controller;

import com.businessapi.dto.request.CustomerSaveDTO;
import com.businessapi.dto.request.CustomerUpdateDTO;
import com.businessapi.dto.request.PageRequestDTO;
import com.businessapi.dto.request.PageRequestTestDTO;
import com.businessapi.dto.response.CustomerResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Customer;
import com.businessapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.EndPoints.*;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(SAVE)
    @Operation(summary = "Save customer", description = "Save customer")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody CustomerSaveDTO customerSaveDTO) {

        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.save(customerSaveDTO))
                .code(200)
                .message("Customer saved successfully").build());

    }

    @GetMapping(FINDALL)
    @Operation(summary = "Find all customers", description = "Find all customers")
    public ResponseEntity<ResponseDTO<List<Customer>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<List<Customer>>builder()
                .data(customerService.findAll(dto))
                .code(200)
                .message("Customers found successfully")
                .build());
    }


    @PutMapping(UPDATE)
    @Operation(summary = "Update customer by token",description = "Update customer by token")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.update(customerUpdateDTO))
                .code(200)
                .message("Customer updated successfully").build());

    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Delete customer by token",description = "Delete customer by token")
    public ResponseEntity<ResponseDTO<Boolean>> deleteById(@RequestParam Long customerId) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.delete(customerId))
                .code(200)
                .message("Customer deleted successfully").build());
    }






}
