package com.rutaaprendizaje.webflux.domain.ports.in;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityTechnologyServicePort {
    Mono<CapabilityWithTechnologiesModel> saveAll(Mono<LinkedCapabilityTechnologyModel> linkModel);
}
