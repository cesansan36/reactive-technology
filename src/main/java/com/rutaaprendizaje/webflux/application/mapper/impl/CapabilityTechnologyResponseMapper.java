package com.rutaaprendizaje.webflux.application.mapper.impl;

import com.rutaaprendizaje.webflux.application.dto.response.CapabilityWithTechnologiesResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;

import java.util.ArrayList;
import java.util.List;

public class CapabilityTechnologyResponseMapper implements ICapabilityTechnologyResponseMapper {

    @Override
    public CapabilityWithTechnologiesResponse toCapabilityWithTechnologiesResponse(CapabilityWithTechnologiesModel capabilityWithTechnologiesModel) {
        CapabilityWithTechnologiesResponse capabilityWithTechnologiesResponse = new CapabilityWithTechnologiesResponse();
        capabilityWithTechnologiesResponse.setCapabilityId(capabilityWithTechnologiesModel.getCapabilityId());

        List<TechnologyInCapabilityResponse> technologies = new ArrayList<>();

        capabilityWithTechnologiesModel
                .getTechnologies()
                .forEach(technology -> technologies.add(new TechnologyInCapabilityResponse(technology.getId(), technology.getName())));

        capabilityWithTechnologiesResponse.setTechnologies(technologies);

        return capabilityWithTechnologiesResponse;
    }
}
