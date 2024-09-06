package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CustomerSaveDTO;
import com.bilgeadam.dto.request.CustomerUpdateDTO;
import com.bilgeadam.dto.response.CustomerResponseDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.EndPoints.*;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(SAVE)
    @Operation(summary = "Save customer",description = "Save customer")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody CustomerSaveDTO customerSaveDTO) {

        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.save(customerSaveDTO))
                .code(200)
                .message("Customer saved successfully").build());

    }

    @GetMapping(FINDALL)
    @Operation(summary = "Find all customers",description = "Find all customers")
    public ResponseEntity<ResponseDTO<List<CustomerResponseDTO>>> findAll() {
        return ResponseEntity.ok(ResponseDTO.<List<CustomerResponseDTO>>builder()
                .data(customerService.findAll())
                .code(200)
                .message("Customers found successfully").build());
    }
    @GetMapping(FINDBYID)
    @Operation(summary = "Find customer by id",description = "Find customer by id")
    public ResponseEntity<ResponseDTO<CustomerResponseDTO>> findById(@RequestParam Long id) {
        return ResponseEntity.ok(ResponseDTO.<CustomerResponseDTO>builder()
                .data(customerService.findById(id))
                .code(200)
                .message("Customer found successfully").build());
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
    public ResponseEntity<ResponseDTO<Boolean>> deleteById(@RequestParam String token) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(customerService.delete(token))
                .code(200)
                .message("Customer deleted successfully").build());
    }



}
