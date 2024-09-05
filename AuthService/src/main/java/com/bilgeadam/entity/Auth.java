package com.bilgeadam.entity;

import com.bilgeadam.utilty.enums.EStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblauths")
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Email
    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
   private EStatus status=EStatus.PENDING;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    private LocalDate updateAt;


}
