package com.rutaaprendizaje.webflux.application.mapper.impl;

import com.rutaaprendizaje.webflux.application.dto.response.TechnologyForCapabilityResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

public class TechnologyResponseMapper implements ITechnologyResponseMapper {

    @Override
    public TechnologyResponse toTechnologyResponse(TechnologyModel technologyModel) {
        return new TechnologyResponse(technologyModel.getId(), technologyModel.getName(), technologyModel.getDescription());
    }

    @Override
    public TechnologyForCapabilityResponse toTechnologyForCapabilityResponse(TechnologyModel technologyModel) {
        return new TechnologyForCapabilityResponse(technologyModel.getId(), technologyModel.getName());
    }
}
