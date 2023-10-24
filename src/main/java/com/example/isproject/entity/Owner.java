package com.example.isproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "owner")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Owner {
    @Id
    private Integer id;
    @Column(value = "name")
    private String name;
    @Column(value = "telephone_number")
    private String telephone_number;
}
