package com.bilgeadam.controller;

import static com.bilgeadam.constants.EndPoints.*;

import com.bilgeadam.dto.request.LoginRequestDTO;
import com.bilgeadam.dto.request.RegisterRequestDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.entity.Auth;
import com.bilgeadam.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE})
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTER)
    @Operation( summary = "Register a new auth",
            description = "Registers a new auth in the system. The user details must be provided in the request body.")
   public ResponseEntity<ResponseDTO<Boolean>> register(@RequestBody RegisterRequestDTO dto){
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
               .data(authService.register(dto))
               .code(200)
               .message("Succesfully registered")
               .build());

   }

   @PostMapping(VERIFYACCOUNT)
   @Operation(
           summary = "Verify  account",
           description = "Verifies the account associated with the provided email address. The email address must be passed as a query parameter. This operation updates the account status to active if the email is found and successfully verified.\" "
   )
   public ResponseEntity<ResponseDTO<Boolean>> verifyAccount(@RequestParam String email) {
       return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
               .data(authService.verifyAccount(email))
               .code(200)
               .message("Succesfully verified")
               .build());
   }


   @PostMapping(LOGIN)
   @Operation(
           summary = "Login a user",
           description = "Logs in a user with the provided credentials. The credentials must be provided in the request body."
   )
   public ResponseEntity<ResponseDTO<String>> login(@RequestBody LoginRequestDTO dto){

       return ResponseEntity.ok(ResponseDTO.<String>builder()
               .code(200)
               .message("Succesfully logged in")
               .data(authService.login(dto))
               .build());
   }


}
