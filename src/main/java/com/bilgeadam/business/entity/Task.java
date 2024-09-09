package com.bilgeadam.business.entity;

import com.bilgeadam.business.entity.enums.EPriority;
import com.bilgeadam.business.entity.enums.EStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbltasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private Long projectId;
    private String name;
    private String description;
    private Long assignedUserId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    EPriority priority;

    @Enumerated(EnumType.STRING)
    EStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore //sonsuz döngüyü engeller
    private Project project;


}
