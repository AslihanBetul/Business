package com.bilgeadam.businessapi.repository;

import com.bilgeadam.businessapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {


}
