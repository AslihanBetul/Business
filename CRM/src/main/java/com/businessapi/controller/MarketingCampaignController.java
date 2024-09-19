package com.businessapi.controller;

import com.businessapi.dto.request.MarketingCampaignSaveDTO;
import com.businessapi.dto.request.MarketingCampaignUpdateDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.MarketingCampaign;
import com.businessapi.service.MarketingCampeignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.EndPoints.*;

@RestController
@RequestMapping(MARKETINGCAMPAIGN)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class MarketingCampaignController {
    private final MarketingCampeignService marketingCampeignService;

    @PostMapping(SAVE)
    @Operation(summary = "Save marketing campaign", description = "Save marketing campaign")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody MarketingCampaignSaveDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(marketingCampeignService.save(dto))
                .code(200)
                .message("Marketing campaign saved successfully")
                .build());
    }
    @GetMapping(FINDALL)
    @Operation(summary = "Find all marketing campaigns", description = "Find all marketing campaigns")
    public ResponseEntity<ResponseDTO<List<MarketingCampaign>>> findAll() {
        return ResponseEntity.ok(ResponseDTO.<List<MarketingCampaign>>builder()
                .data(marketingCampeignService.findAll())
                .code(200)
                .message("Marketing campaigns found successfully")
                .build());
    }
    @PostMapping(UPDATE)
    @Operation(summary = "Update marketing campaign by id",description = "Update marketing campaign by id")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody MarketingCampaignUpdateDTO dto) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(marketingCampeignService.update(dto))
                .code(200)
                .message("Marketing campaign updated successfully")
                .build());
    }

    @DeleteMapping(DELETE)
    @Operation(summary = "Delete marketing campaign",description = "Delete marketing campaign")
    public ResponseEntity<ResponseDTO<Boolean>> delete(@RequestParam Long id) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(marketingCampeignService.delete(id))
                .code(200)
                .message("Marketing campaign deleted successfully")
                .build());
    }

}
