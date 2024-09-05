package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_customer")
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private Long authId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;



}
