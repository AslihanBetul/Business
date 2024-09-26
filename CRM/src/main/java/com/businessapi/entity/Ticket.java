package com.businessapi.entity;

import com.businessapi.utility.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tblticket")
public class Ticket extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long memberId;
    private Long customerId;
    private String subject;
    private String description;
    private String ticketStatus;
    private String priority;
    private LocalDateTime createdDate;
    private LocalDateTime closedDate;
    @Enumerated(EnumType.STRING)
    EStatus status;
}
