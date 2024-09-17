package com.bilgeadam.businessapi.service;

import com.bilgeadam.businessapi.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.businessapi.dto.response.ProjectResponseDto;
import com.bilgeadam.businessapi.entity.Project;
import com.bilgeadam.businessapi.mapper.ProjectMapper;
import com.bilgeadam.businessapi.repository.ProjectRepository;
import com.bilgeadam.businessapi.repository.TaskRepository;
import com.bilgeadam.businessapi.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService extends ServiceManager<Project,Long> {

private final ProjectRepository projectRepository;
private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final ProjectMapper projectMapper;


    public ProjectService(ProjectRepository projectRepository, TaskService taskService, TaskRepository taskRepository, ProjectMapper projectMapper) {
        super(projectRepository);
        this.projectRepository = projectRepository;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.projectMapper = projectMapper;
    }


    public Project saveProject(ProjectSaveRequestDTO dto) {

        Project kaydedilecekProje = Project.builder().id(dto.id()).name(dto.name()).description(dto.description()).status(dto.status()).build();
        projectRepository.save(kaydedilecekProje);
        return kaydedilecekProje;

    }

    public List<ProjectResponseDto> findAllProjects() {

        return projectMapper.projectToProjectFindAllResponseDto(projectRepository.findAll());
    }


    public void updateProject(ProjectSaveRequestDTO dto) {
        Project proje = projectRepository.findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("Proje bulunamadı: " + dto.id()));

        proje.setName(dto.name());
        proje.setDescription(dto.description());
        proje.setStatus(dto.status());

        projectRepository.save(proje);
    }
}
