package com.bilgeadam.business.controller;

import com.bilgeadam.business.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.business.dto.request.TaskSaveRequestDTO;
import com.bilgeadam.business.entity.Project;
import com.bilgeadam.business.entity.Task;
import com.bilgeadam.business.service.ProjectService;
import com.bilgeadam.business.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.business.constant.EndPoints.*;

@RestController
@RequestMapping(ROOT + TASK)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(SAVE)
    @CrossOrigin("*")
    public ResponseEntity<String> saveTask(@RequestBody TaskSaveRequestDTO taskSaveRequestDTO) {
        taskService.saveTask(taskSaveRequestDTO);
        return ResponseEntity.ok("Task kaydedildi");

    }


}
