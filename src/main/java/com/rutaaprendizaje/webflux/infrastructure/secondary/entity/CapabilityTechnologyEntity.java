package com.rutaaprendizaje.webflux.infrastructure.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technology")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CapabilityTechnologyEntity {

    @Id
    private Long id;

    @Column("capability_id")
    private Long capabilityId;

    @Column("technology_id")
    private Long technologyId;
}
