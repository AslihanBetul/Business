package com.businessapi.controller;

import com.businessapi.constants.EndPoints;
import com.businessapi.dto.request.EventDeleteRequestDTO;
import com.businessapi.dto.request.EventSaveRequestDTO;
import com.businessapi.dto.request.EventUpdateRequestDTO;
import com.businessapi.dto.response.ResponseDTO;
import com.businessapi.entity.Event;
import com.businessapi.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.businessapi.constants.EndPoints.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(EndPoints.EVENT)
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;

    @PostMapping(SAVE)
    @Operation(summary = "Create event", description = "Create event")
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody EventSaveRequestDTO eventSaveRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.save(eventSaveRequestDTO))
                .code(200)
                .message("Event saved successfully").build());
    }

    @DeleteMapping(DELETE_EVENT_BY_CREATOR)
    @Operation(summary = "Delete event by creator", description = "Delete event created by the user")
    public ResponseEntity<ResponseDTO<Boolean>> deleteByCreator(@RequestBody EventDeleteRequestDTO eventDeleteRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.deleteByCreator(eventDeleteRequestDTO))
                .code(200)
                .message("Event deleted successfully by creator").build());
    }

    @DeleteMapping(DELETE_EVENT_BY_INVITEE)
    @Operation(summary = "Delete event by invitee", description = "Delete event only for the invited user")
    public ResponseEntity<ResponseDTO<Boolean>> deleteByInvitee(@RequestBody EventDeleteRequestDTO eventDeleteRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.deleteByInvitee(eventDeleteRequestDTO))
                .code(200)
                .message("Event deleted successfully by invitee").build());
    }

    @GetMapping(FIND_ALL_BY_AUTH_ID)
    @Operation(summary = "Find all events by auth id", description = "Find all events by auth id")
    public ResponseEntity<ResponseDTO<List<Event>>> findAll(@RequestParam String token) {
        return ResponseEntity.ok(ResponseDTO.<List<Event>>builder()
                .data(eventService.findAllByAuthId(token))
                .code(200)
                .message("Events found successfully")
                .build());
    }
    @PutMapping(UPDATE)
    @Operation(summary = "Update event by token", description = "Update event by token")
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody EventUpdateRequestDTO eventUpdateRequestDTO) {
        return ResponseEntity.ok(ResponseDTO.<Boolean>builder()
                .data(eventService.update(eventUpdateRequestDTO))
                .code(200)
                .message("Event updated successfully").build());
    }
}
