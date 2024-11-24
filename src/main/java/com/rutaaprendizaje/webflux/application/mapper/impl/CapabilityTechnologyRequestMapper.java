package com.rutaaprendizaje.webflux.application.mapper.impl;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;

public class CapabilityTechnologyRequestMapper implements ICapabilityTechnologyRequestMapper {
    @Override
    public LinkedCapabilityTechnologyModel toCapabilityModel(LinkCapabilityWithTechnologiesRequest linkCapabilityWithTechnologiesRequest) {
        return new LinkedCapabilityTechnologyModel(
                linkCapabilityWithTechnologiesRequest.capabilityId(),
                linkCapabilityWithTechnologiesRequest.technologiesNames()
        );
    }
}
