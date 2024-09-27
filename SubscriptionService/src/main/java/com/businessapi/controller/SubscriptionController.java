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

import static com.businessapi.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + SUBSCRIPTION)
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping(SAVE)
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @Operation(summary = "Starts new subscription", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseDTO<Subscription>> saveSubscription(@RequestBody SubscriptionSaveRequestDTO dto){
        return ResponseEntity.ok(ResponseDTO
                .<Subscription>builder()
                .data(subscriptionService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }
}
