package com.businessapi.controller;

import com.businessapi.dto.request.SubscriptionSaveRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Subscription;
import com.businessapi.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + SUBSCRIPTION)
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping(SUBSCRIBE)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @Operation(summary = "Starts new subscription", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseDTO<Subscription>> saveSubscription(@RequestBody SubscriptionSaveRequestDTO dto){
        return ResponseEntity.ok(ResponseDTO
                .<Subscription>builder()
                .data(subscriptionService.subscribe(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(UNSUBSCRIBE)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @Operation(summary = "Cancels subscription", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseDTO<Boolean>> cancelSubscription(@RequestParam Long planId ){
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(subscriptionService.unsubscribe(planId))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(CHECK_SUBSCRIPTIONS)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @Operation(summary = "Checks subscriptions", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseDTO<List<String>>> checkSubscriptions(){
        return ResponseEntity.ok(ResponseDTO
                .<List<String>>builder()
                .data(subscriptionService.checkSubscriptions())
                .message("Success")
                .code(200)
                .build());
    }
}