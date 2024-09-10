package com.financeservice.entity;

import com.financeservice.entity.enums.EInvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblinvoice")
public class Invoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long customerIdOrSupplierId;
    LocalDateTime invoiceDate;
    BigDecimal totalAmount;
    BigDecimal paidAmount;
    @Enumerated(EnumType.STRING)
    EInvoiceStatus invoiceStatus;
    String description;
}
