package com.bilgeadam.business.controller;

import com.bilgeadam.business.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.business.entity.Project;
import com.bilgeadam.business.service.ProjectService;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.business.constant.EndPoints.*;

@RestController
@RequestMapping(ROOT+PROJECT)
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(SAVE)
    @CrossOrigin("*")
    public ResponseEntity<String> saveProject(@RequestBody ProjectSaveRequestDTO dto) {

        projectService.saveProject(dto);
        return ResponseEntity.ok("Proje kaydedildi");

    }

    @GetMapping(FINDALL)
    @CrossOrigin("*")
    public ResponseEntity<List<Project>> findAllProjects(){
        return ResponseEntity.ok(projectService.findAll());

    }


    @PutMapping(UPDATE)
    @CrossOrigin("*")
    public ResponseEntity<Project> updateProject(@RequestBody Project updateProject){
        return ResponseEntity.ok(projectService.update(updateProject));
    }


    @DeleteMapping(DELETE)
    @CrossOrigin("*")
    public ResponseEntity<String> deleteProject(@RequestBody Long projectId){
        projectService.deleteById(projectId);
        return ResponseEntity.ok("proje silindi");
    }




}
