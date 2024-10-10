package com.businessapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblemployee")
public class Employee extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long memberId;
    Long authId;
    @ManyToOne
    Employee manager;
    @ManyToOne
    Department department;
    @OneToMany(cascade = CascadeType.ALL)
    List<Employee> subordinates;
    String identityNo;
    String phoneNo;
    String name;
    String surname;
    String email;
}
