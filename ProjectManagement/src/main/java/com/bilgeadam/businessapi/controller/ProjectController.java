package com.bilgeadam.businessapi.controller;

import com.bilgeadam.businessapi.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.businessapi.dto.response.ProjectResponseDto;
import com.bilgeadam.businessapi.dto.response.ProjectUpdateResponseDto;
import com.bilgeadam.businessapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.businessapi.constant.EndPoints.*;

@RestController
@RequestMapping(ROOT+PROJECT)
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(SAVE)
    @CrossOrigin("*")
    public ResponseEntity<ProjectResponseDto> saveProject(@RequestBody ProjectSaveRequestDTO dto) {

        projectService.saveProject(dto);

        return ResponseEntity.ok(ProjectResponseDto.builder().id(dto.id()).name(dto.name()).build());
    }

    @GetMapping(FINDALL)
    @CrossOrigin("*")
    public ResponseEntity<List<ProjectResponseDto>> findAllProjects(){

        return ResponseEntity.ok(projectService.findAllProjects());

    }

    @PutMapping(UPDATE)
    @CrossOrigin("*")
    public ResponseEntity<ProjectUpdateResponseDto> update(@RequestBody ProjectSaveRequestDTO dto){
        projectService.updateProject(dto);
        return ResponseEntity.ok(ProjectUpdateResponseDto.builder().id(dto.id()).name(dto.name()).description(dto.description()).status(dto.status()).build());
    }

    @DeleteMapping(DELETE)
    @CrossOrigin("*")
    public ResponseEntity<String> deleteProject(@RequestBody Long id){
        projectService.deleteById(id);
        return ResponseEntity.ok("proje silindi");

    }








}
