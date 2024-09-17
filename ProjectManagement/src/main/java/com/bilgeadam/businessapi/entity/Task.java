package com.bilgeadam.businessapi.entity;

import com.bilgeadam.businessapi.entity.enums.EPriority;
import com.bilgeadam.businessapi.entity.enums.EResources;
import com.bilgeadam.businessapi.entity.enums.EStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(length = 60)
    private String description;
    private Long assignedUserId;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    EPriority priority;

    @Enumerated(EnumType.STRING)
    EStatus status;


    @ElementCollection(targetClass = EResources.class)
    @CollectionTable(name = "task_resources", joinColumns = @JoinColumn(name = "task_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "resource")
    private List<EResources> resources;


    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore //sonsuz döngüyü engeller
    private Project project;








}
