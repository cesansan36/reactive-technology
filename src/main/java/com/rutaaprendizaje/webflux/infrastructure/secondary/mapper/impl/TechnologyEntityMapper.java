package com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.impl;

import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.TechnologyEntity;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ITechnologyEntityMapper;

public class TechnologyEntityMapper implements ITechnologyEntityMapper {

    @Override
    public TechnologyModel toTechnologyModel(TechnologyEntity technologyEntity) {
        return new TechnologyModel(technologyEntity.getId(), technologyEntity.getName(), technologyEntity.getDescription());
    }

    @Override
    public TechnologyEntity toTechnologyEntity(TechnologyModel technologyModel) {
        return new TechnologyEntity(technologyModel.getId(), technologyModel.getName(), technologyModel.getDescription());
    }
}
