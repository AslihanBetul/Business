package com.bilgeadam.business.repository;

import com.bilgeadam.business.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {

}
