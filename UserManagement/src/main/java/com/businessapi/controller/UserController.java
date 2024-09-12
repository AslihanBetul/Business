package com.businessapi.controller;

import com.businessapi.constants.EndPoints;
import com.businessapi.constants.messages.SuccesMessages;
import com.businessapi.dto.requestDTOs.AddRoleToUserRequestDTO;
import com.businessapi.dto.requestDTOs.UserDeleteRequestDTO;
import com.businessapi.dto.requestDTOs.UserSaveRequestDTO;
import com.businessapi.dto.requestDTOs.UserUpdateRequestDTO;
import com.businessapi.dto.responseDTOs.GetAllUsersResponseDTO;
import com.businessapi.dto.responseDTOs.ResponseDTO;
import com.businessapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.USER)
public class UserController {
    private final UserService userService;



    @PostMapping(EndPoints.SAVE)
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Operation(summary = "Admin tarafından kullanıcı oluşturma")
    public ResponseEntity<ResponseDTO<Boolean>> saveUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO){
        userService.saveUser(userSaveRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_SAVED).build());
    }



    @PutMapping(EndPoints.UPDATE)
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','UNASSIGNED')")
    @Operation(summary = "AuthId'si verilen kullanıcıların bilgilerinin güncellenmesi")
    public ResponseEntity<ResponseDTO<Boolean>> updateUser(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO){
        userService.updateUser(userUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_UPDATED).build());
    }


    @PutMapping(EndPoints.DELETE)
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Operation(summary = "AuthId'si verilen kullanıcının soft delete'i")
    public ResponseEntity<ResponseDTO<Boolean>> deleteUser(@RequestBody UserDeleteRequestDTO userDeleteRequestDTO){
        userService.deleteUser(userDeleteRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_DELETED).build());
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/get-all-users")
    @Operation(summary = "Tüm kuullanıcıları getirir, adminin rol atamasi için ilk yöntem")
    public ResponseEntity<ResponseDTO<List<GetAllUsersResponseDTO>>> getAllUsers(){
        return ResponseEntity.ok(ResponseDTO.<List<GetAllUsersResponseDTO>>builder().code(200).data(userService.getAllUser()).message("All users sent").build());
    }


    @PutMapping("/add-role-to-user")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @Operation(summary = "Kullanıcıya admin tarafından rol ekleme")
    public ResponseEntity<ResponseDTO<Boolean>> addRoleToUser(@RequestBody AddRoleToUserRequestDTO addRoleToUserRequestDTO){
        userService.addRoleToUser(addRoleToUserRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message("Role added to user").build());
    }


    @GetMapping("/get-user-roles")
    public ResponseEntity<ResponseDTO<List<String>>> getAllUsersRoles(@RequestHeader("Authorization") String token){
        String jwtToken = token.replace("Bearer ", "");
        return ResponseEntity.ok(ResponseDTO.<List<String>>builder().code(200).message("User roles sent").data(userService.getUserRoles(jwtToken)).build());
    }





}
