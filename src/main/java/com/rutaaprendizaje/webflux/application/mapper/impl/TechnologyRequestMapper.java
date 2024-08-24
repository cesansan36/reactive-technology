package com.rutaaprendizaje.webflux.application.mapper.impl;

import com.rutaaprendizaje.webflux.application.dto.request.SaveTechnologyRequest;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyRequestMapper;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

public class TechnologyRequestMapper implements ITechnologyRequestMapper {

    @Override
    public TechnologyModel toTechnologyModel(SaveTechnologyRequest saveTechnologyRequest) {
        return new TechnologyModel(null, saveTechnologyRequest.getName(), saveTechnologyRequest.getDescription());
    }
}
