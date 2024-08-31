package com.rutaaprendizaje.webflux.domain.model;

import java.util.List;

public class CapabilityWithTechnologiesModel {

    private Long capabilityId;
    private List<TechnologyModel> technologies;

    public CapabilityWithTechnologiesModel(Long capabilityId, List<TechnologyModel> technologies) {
        this.capabilityId = capabilityId;
        this.technologies = technologies;
    }

    public CapabilityWithTechnologiesModel() {
    }

    public Long getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(Long capabilityId) {
        this.capabilityId = capabilityId;
    }

    public List<TechnologyModel> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<TechnologyModel> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "CapabilityWithTechnologiesModel{" +
                "capabilityId=" + capabilityId +
                ", technologies=" + technologies +
                '}';
    }
}
