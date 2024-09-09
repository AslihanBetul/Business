package com.usermanagement.controller;

import com.usermanagement.constants.EndPoints;
import com.usermanagement.constants.messages.SuccesMessages;
import com.usermanagement.dto.requestDTOs.AddRoleToUserRequestDTO;
import com.usermanagement.dto.requestDTOs.UserDeleteRequestDTO;
import com.usermanagement.dto.requestDTOs.UserSaveRequestDTO;
import com.usermanagement.dto.requestDTOs.UserUpdateRequestDTO;
import com.usermanagement.dto.responseDTOs.GetAllUsersResponseDTO;
import com.usermanagement.dto.responseDTOs.ResponseDTO;
import com.usermanagement.entity.Role;
import com.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.USER)
public class UserController {
    private final UserService userService;


    @PostMapping(EndPoints.SAVE)
    @Operation(summary = "Admin tarafından kullanıcı oluşturma")
    public ResponseEntity<ResponseDTO<Boolean>> saveUser(@RequestBody UserSaveRequestDTO userSaveRequestDTO){
        userService.saveUser(userSaveRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_SAVED).build());
    }



    @PutMapping(EndPoints.UPDATE)
    @Operation(summary = "AuthId'si verilen kullanıcıların bilgilerinin güncellenmesi")
    public ResponseEntity<ResponseDTO<Boolean>> updateUser(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO){
        userService.updateUser(userUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_UPDATED).build());
    }


    @PutMapping(EndPoints.DELETE)
    @Operation(summary = "AuthId'si verilen kullanıcının soft delete'i")
    public ResponseEntity<ResponseDTO<Boolean>> deleteUser(@RequestBody UserDeleteRequestDTO userDeleteRequestDTO){
        userService.deleteUser(userDeleteRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message(SuccesMessages.USER_DELETED).build());
    }

    @GetMapping("/get-all-users")
    @Operation(summary = "Tüm kuullanıcıları getirir, adminin rol atamasi için ilk yöntem")
    public ResponseEntity<ResponseDTO<List<GetAllUsersResponseDTO>>> getAllUsers(){
        return ResponseEntity.ok(ResponseDTO.<List<GetAllUsersResponseDTO>>builder().code(200).data(userService.getAllUser()).message("All users sent").build());
    }


    @PutMapping("/add-role-to-user")
    @Operation(summary = "Kullanıcıya admin tarafından rol ekleme")
    public ResponseEntity<ResponseDTO<Boolean>> addRoleToUser(@RequestBody AddRoleToUserRequestDTO addRoleToUserRequestDTO){
        userService.addRoleToUser(addRoleToUserRequestDTO);
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder().code(200).message("Role added to user").build());
    }

    @GetMapping("/getttttt-rolleeees")
    private ResponseEntity<ResponseDTO<List<String>>> getRolesrabit(@RequestParam Long authId){
        return ResponseEntity.ok(ResponseDTO.<List<String>>builder().code(200).data(userService.getRolesRabbit(authId)).build());
    }



}
