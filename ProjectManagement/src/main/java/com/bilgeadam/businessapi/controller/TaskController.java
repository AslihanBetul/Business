package com.bilgeadam.businessapi.controller;

import com.bilgeadam.businessapi.dto.request.TaskSaveRequestDTO;
import com.bilgeadam.businessapi.dto.response.TaskSaveResponseDTO;
import com.bilgeadam.businessapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.businessapi.constant.EndPoints.*;

@RestController
@RequestMapping(ROOT + TASK)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping(SAVED)
    @CrossOrigin("*")
    public ResponseEntity<TaskSaveResponseDTO> saveTask(@RequestBody TaskSaveRequestDTO dto) {
        TaskSaveResponseDTO response = taskService.saveTask(dto);
        return ResponseEntity.ok(response);
    }

    }





