package com.example.isproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(value = "pet")
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Pet {
    @Id
    private Long id;
    @Column(value = "name")
    private String name;
    @Column(value = "species")
    private String species;
    @Column(value = "birthdate")
    private LocalDate birthdate;
    @Column(value = "weight")
    private Integer weight;
    @Column(value = "owner_id")
    private Long owner_id;
}
