package com.wasp203.core_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    String name;

    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "group_number", nullable = false)
    String groupNumber;
}
