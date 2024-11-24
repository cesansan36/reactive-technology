package com.rutaaprendizaje.webflux.infrastructure.secondary.mapper;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.CapabilityTechnologyEntity;

public interface ICapabilityTechnologyEntityMapper {
    CapabilityTechnologyModel toModel(CapabilityTechnologyEntity entity);

    CapabilityTechnologyEntity toEntity(CapabilityTechnologyModel model);
}
