package com.bilgeadam.business.repository;

import com.bilgeadam.business.entity.Project;
import com.bilgeadam.business.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
