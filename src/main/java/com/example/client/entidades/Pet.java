package com.example.client.entidades;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private Long id;
    private String name;
    private String species;
    private LocalDate birthday;
    private Integer weight;
    private Long owner_id;
}
