package com.bilgeadam.businessapi.service;

import com.bilgeadam.businessapi.dto.request.TaskSaveRequestDTO;
import com.bilgeadam.businessapi.dto.response.TaskSaveResponseDTO;
import com.bilgeadam.businessapi.entity.Project;
import com.bilgeadam.businessapi.entity.Task;
import com.bilgeadam.businessapi.repository.ProjectRepository;
import com.bilgeadam.businessapi.repository.TaskRepository;
import com.bilgeadam.businessapi.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService extends ServiceManager<Task,Long> {

private final TaskRepository taskRepository;
private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskSaveResponseDTO saveTask(TaskSaveRequestDTO dto) {
        // İlgili projeyi buluyoruz
        Optional<Project> projectOpt = projectRepository.findById(dto.projectId());
        if (projectOpt.isEmpty()) {
            throw new RuntimeException("Proje bulunamadı");
        }

        Project project = projectOpt.get();

        // Task'ı oluşturarak Project ile ilişkilendirdik
        Task taskToSave = Task.builder()
                .name(dto.name())
                .description(dto.description())
                .assignedUserId(dto.assignedUserId())
                .priority(dto.priority())
                .status(dto.status())
                //.resources(dto.resources())
                .project(project) // İlgili project'e bağlanma kısmı
                .build();


        Task savedTask = taskRepository.save(taskToSave);


        return TaskSaveResponseDTO.builder()
                .name(savedTask.getName())
                .description(savedTask.getDescription())
                .assignedUserId(savedTask.getAssignedUserId())
                .priority(savedTask.getPriority())
                .status(savedTask.getStatus())
               //.resources(savedTask.getResources())
                .projectId(savedTask.getProject().getId())  // Proje id'sini set et
                .build();

    }
}
