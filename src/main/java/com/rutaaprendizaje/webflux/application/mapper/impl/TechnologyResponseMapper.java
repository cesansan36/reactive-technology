package com.rutaaprendizaje.webflux.application.mapper.impl;

import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

public class TechnologyResponseMapper implements ITechnologyResponseMapper {

    @Override
    public TechnologyResponse toTechnologyResponse(TechnologyModel technologyModel) {
        return new TechnologyResponse(technologyModel.getId(), technologyModel.getName(), technologyModel.getDescription());
    }
}
