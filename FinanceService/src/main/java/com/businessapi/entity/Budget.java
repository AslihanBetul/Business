package com.businessapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblbudget")
public class Budget extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String department;
    Integer year;
    BigDecimal amount;
    @Builder.Default
    BigDecimal spentAmount = new BigDecimal(0);
    String description;
}
