package com.bilgeadam.business.service;

import com.bilgeadam.business.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.business.entity.Project;
import com.bilgeadam.business.entity.Task;
import com.bilgeadam.business.repository.ProjectRepository;
import com.bilgeadam.business.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService extends ServiceManager<Project,Long> {

private final ProjectRepository projectRepository;
private final TaskService taskService;


    public ProjectService(ProjectRepository projectRepository, TaskService taskService) {
        super(projectRepository);
        this.projectRepository = projectRepository;
        this.taskService = taskService;
    }

    //@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Project saveProject(ProjectSaveRequestDTO dto) {

        Project kaydedilecekProje = Project.builder().name(dto.name()).description(dto.description()).status(dto.status()).build();
        projectRepository.save(kaydedilecekProje);


        // Görevleri kaydet ve proje ile ilişkilendir
        List<Task> tasks = new ArrayList<>();
        for (Task task : tasks) {
            task.setProjectId(kaydedilecekProje.getProjectId()); // Proje ID'sini görev ile ilişkilendir
            taskService.save(task); // Görevi kaydet
        }

        return kaydedilecekProje;
    }




//    public void saveProject(ProjectSaveRequestDTO dto) {
//
//        Project kaydedilecekProje = Project.builder().name(dto.name()).description(dto.description()).status(dto.status()).build();
//        projectRepository.save(kaydedilecekProje);
//    }






//    public Object delete(Long projectId) {
//        Project proje = projectRepository.findById(projectId).orElse(null);
//        if (proje!= null) {
//            projectRepository.delete(proje);
//            return "Proje silindi";
//        } else {
//            return "Proje bulunamadı";
//        }
//    }
}
