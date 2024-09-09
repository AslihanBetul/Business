package com.bilgeadam.business.service;

import com.bilgeadam.business.dto.request.ProjectSaveRequestDTO;
import com.bilgeadam.business.dto.request.TaskSaveRequestDTO;
import com.bilgeadam.business.entity.Project;
import com.bilgeadam.business.entity.Task;
import com.bilgeadam.business.repository.ProjectRepository;
import com.bilgeadam.business.repository.TaskRepository;
import com.bilgeadam.business.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class TaskService extends ServiceManager<Task,Long> {

private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    public void saveTask(TaskSaveRequestDTO taskSaveRequestDTO) {

        Task kaydedilecekTask = Task.builder().projectId(taskSaveRequestDTO.projectId()).name(taskSaveRequestDTO.name()).description(taskSaveRequestDTO.description())
                .assignedUserId(taskSaveRequestDTO.assignedUserId())
                .priority(taskSaveRequestDTO.priority()).status(taskSaveRequestDTO.status()).build();

        taskRepository.save(kaydedilecekTask);
    }




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
