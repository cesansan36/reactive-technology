package com.rutaaprendizaje.webflux.infrastructure.secondary.mapper;

import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.TechnologyEntity;

public interface ITechnologyEntityMapper {
    TechnologyModel toTechnologyModel(TechnologyEntity technologyEntity);

    TechnologyEntity toTechnologyEntity(TechnologyModel technologyModel);
}
