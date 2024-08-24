package com.rutaaprendizaje.webflux.application.mapper;

import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

public interface ITechnologyResponseMapper {
    TechnologyResponse toTechnologyResponse(TechnologyModel technologyModel);
}
