package com.businessapi.entity;

import com.businessapi.utility.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tblopportunity")
@EqualsAndHashCode(callSuper = true)
public class Opportunity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long memberId;
    private Long customerId;
    private String name;
    private String description;
    private Double value;
    private String stage;
    private Double probability;
    @Enumerated(EnumType.STRING)
    EStatus status;
}
