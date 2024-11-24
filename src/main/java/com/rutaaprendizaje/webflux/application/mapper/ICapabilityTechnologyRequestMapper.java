package com.rutaaprendizaje.webflux.application.mapper;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;

public interface ICapabilityTechnologyRequestMapper {
    LinkedCapabilityTechnologyModel toCapabilityModel(LinkCapabilityWithTechnologiesRequest linkCapabilityWithTechnologiesRequest);
}
