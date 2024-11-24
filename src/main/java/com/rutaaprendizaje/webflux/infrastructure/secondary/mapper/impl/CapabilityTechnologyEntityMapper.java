package com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.impl;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.CapabilityTechnologyEntity;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ICapabilityTechnologyEntityMapper;

public class CapabilityTechnologyEntityMapper implements ICapabilityTechnologyEntityMapper {

    @Override
    public CapabilityTechnologyModel toModel(CapabilityTechnologyEntity entity) {
        return new CapabilityTechnologyModel(entity.getId(), entity.getCapabilityId(), entity.getTechnologyId());
    }

    @Override
    public CapabilityTechnologyEntity toEntity(CapabilityTechnologyModel model) {
        return new CapabilityTechnologyEntity(model.getId(), model.getCapabilityId(), model.getTechnologyId());
    }
}
