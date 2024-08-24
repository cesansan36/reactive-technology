package com.rutaaprendizaje.webflux.infrastructure.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("technology")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TechnologyEntity {

    @Id
    private Long id;
    private String name;
    private String description;
}
