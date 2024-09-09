package com.usermanagement.controller;

import com.usermanagement.constants.EndPoints;
import com.usermanagement.constants.messages.SuccesMessages;
import com.usermanagement.dto.requestDTOs.RoleCreateDTO;
import com.usermanagement.dto.requestDTOs.RoleUpdateRequestDTO;
import com.usermanagement.dto.responseDTOs.ResponseDTO;
import com.usermanagement.dto.responseDTOs.RoleResponseDTO;
import com.usermanagement.entity.User;
import com.usermanagement.service.RoleService;
import com.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.ROLE)
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;



    @PostMapping(EndPoints.CREATE_USER_ROLE)
    @Operation(summary = "Admin tarafından kullanıcı rolü eklenmesi")
    public ResponseEntity<ResponseDTO<Boolean>> createUserRole(@RequestBody RoleCreateDTO roleCreateDTO){
        roleService.createUserRole(roleCreateDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_CREATED).build());
    }

    @PutMapping(EndPoints.UPDATE_USER_ROLE)
    @Operation(summary = "admin tarafından var olan bir rolun düzenlenmesi")
    public ResponseEntity<ResponseDTO<Boolean>> updateUserRole(@RequestBody RoleUpdateRequestDTO roleUpdateRequestDTO){
        roleService.updateUserRole(roleUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_UPDATED).build());
    }

    @PutMapping(EndPoints.DELETE_USER_ROLE+"/{roleId}")
    @Operation(summary = "admin tarafından roleId'si verilen bir rolün soft delete yapılması")
    public ResponseEntity<ResponseDTO<Boolean>> deleteUserRole(@PathVariable Long roleId){
        roleService.deleteUserRole(roleId);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.ROLE_DELETED).build());
    }


    @GetMapping(EndPoints.GET_ALL_USER_ROLES)
    @Operation(summary = "Admin tarafından bir kullanıcıya rol atanırken rollerin getirilmesi için gerekli get isteği")
    public ResponseEntity<ResponseDTO<List<RoleResponseDTO>>> getAllUserRoles(){

        return ResponseEntity.ok(ResponseDTO.<List<RoleResponseDTO>>builder().code(200).data(roleService.getAllUserRoles()).message(SuccesMessages.All_ROLES_SENT).build());
    }

    @GetMapping("/assignable-roles/{userId}")
    @Operation(summary = "Admin tarafından kullanıcılara atanabilir roller")
    public ResponseEntity<ResponseDTO<List<RoleResponseDTO>>> getAllAssignableRoles(@PathVariable Long userId){
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(ResponseDTO.<List<RoleResponseDTO>>builder().code(200).message("Kullanıcıya atanabilir roller gönderildi").data(roleService.getAllAssignableRoles(user)).build());
    }



}
