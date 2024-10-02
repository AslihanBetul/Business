package com.businessapi.entity;


import com.businessapi.entity.enums.EExpenseCategory;
import com.businessapi.entity.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblexpense")
public class Expense extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    EExpenseCategory expenseCategory;
    LocalDate expenseDate;
    BigDecimal amount;
    String description;
}
