package com.businessapi.entity;

import com.businessapi.utility.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tblsalesactivity")
public class SalesActivity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long memberId;
    private Long opportunityId;
    private String type;
    private LocalDate date;
    private String notes;
    @Enumerated(EnumType.STRING)
    EStatus status;
}
