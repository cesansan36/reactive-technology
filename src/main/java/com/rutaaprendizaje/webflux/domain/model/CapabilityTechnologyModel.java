package com.rutaaprendizaje.webflux.domain.model;

public class CapabilityTechnologyModel {

    private Long id;
    private Long capabilityId;
    private Long technologyId;

    public CapabilityTechnologyModel(Long id, Long capabilityId, Long technologyId) {
        this.id = id;
        this.capabilityId = capabilityId;
        this.technologyId = technologyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(Long capabilityId) {
        this.capabilityId = capabilityId;
    }

    public Long getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }
}
