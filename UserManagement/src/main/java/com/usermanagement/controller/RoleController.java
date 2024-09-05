package com.usermanagement.controller;

import com.usermanagement.constants.EndPoints;
import com.usermanagement.constants.messages.SuccesMessages;
import com.usermanagement.dto.requestDTOs.RoleCreateDTO;
import com.usermanagement.dto.requestDTOs.RoleUpdateRequestDTO;
import com.usermanagement.dto.responseDTOs.ResponseDTO;
import com.usermanagement.dto.responseDTOs.RoleResponseDTO;
import com.usermanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.ROLE)
public class RoleController {
    private final RoleService roleService;



    @PostMapping(EndPoints.CREATE_USER_ROLE)
    public ResponseEntity<ResponseDTO<Boolean>> createUserRole(@RequestBody RoleCreateDTO roleCreateDTO){
        roleService.createUserRole(roleCreateDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_CREATED).build());
    }

    @PutMapping(EndPoints.UPDATE_USER_ROLE)
    public ResponseEntity<ResponseDTO<Boolean>> updateUserRole(@RequestBody RoleUpdateRequestDTO roleUpdateRequestDTO){
        roleService.updateUserRole(roleUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_UPDATED).build());
    }

    @PutMapping(EndPoints.DELETE_USER_ROLE+"/{roleId}")
    public ResponseEntity<ResponseDTO<Boolean>> deleteUserRole(@PathVariable Long roleId){
        roleService.deleteUserRole(roleId);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_DELETED).build());
    }


    @GetMapping(EndPoints.GET_ALL_USER_ROLES)
    public ResponseEntity<ResponseDTO<List<RoleResponseDTO>>> getAllUserRoles(){

        return ResponseEntity.ok(ResponseDTO.<List<RoleResponseDTO>>builder().code(200).data(roleService.getAllUserRoles()).message(SuccesMessages.All_ROLES_SENT).build());
    }


}
