package com.businessapi.controllers;

import com.businessapi.dto.request.*;
import com.businessapi.dto.response.ManagerResponseDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entities.Department;
import com.businessapi.entities.Manager;
import com.businessapi.services.DepartmentService;
import com.businessapi.services.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + MANAGER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagerController
{
    private final ManagerService managerService;


    @PostMapping(SAVE)
    @Operation(summary = "Creates new Manager")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody ManagerSaveRequestDto dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(managerService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Soft deletes Manager")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(managerService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE)
    @Operation(summary = "Updates Manager")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody ManagerUpdateRequestDto dto){

        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(managerService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL)
    @Operation(summary = "Finds all Manager with respect to pagination")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<ResponseDTO<List<ManagerResponseDTO>>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(ResponseDTO
                .<List<ManagerResponseDTO>>builder()
                .data(managerService.findAllByNameContainingIgnoreCaseAndMemberIdAndStatusIsNotOrderByNameAsc(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_BY_ID)
    @Operation(summary = "Finds Manager by Id")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<ResponseDTO<Manager>> findById(Long id){

        return ResponseEntity.ok(ResponseDTO
                .<Manager>builder()
                .data(managerService.findByIdAndMemberId(id))
                .message("Success")
                .code(200)
                .build());
    }




}
