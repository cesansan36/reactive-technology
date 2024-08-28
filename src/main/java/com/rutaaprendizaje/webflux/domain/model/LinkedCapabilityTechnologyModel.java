package com.rutaaprendizaje.webflux.domain.model;

import java.util.List;

public class LinkedCapabilityTechnologyModel {

    private Long id;
    private List<String> technologiesNames;

    public LinkedCapabilityTechnologyModel(Long id, List<String> technologiesNames) {
        this.id = id;
        this.technologiesNames = technologiesNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTechnologiesNames() {
        return technologiesNames;
    }

    public void setTechnologiesNames(List<String> technologiesNames) {
        this.technologiesNames = technologiesNames;
    }
}
