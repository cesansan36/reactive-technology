package com.rutaaprendizaje.webflux.application.mapper;

import com.rutaaprendizaje.webflux.application.dto.request.SaveTechnologyRequest;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

public interface ITechnologyRequestMapper {
    TechnologyModel toTechnologyModel(SaveTechnologyRequest saveTechnologyRequest);
}
