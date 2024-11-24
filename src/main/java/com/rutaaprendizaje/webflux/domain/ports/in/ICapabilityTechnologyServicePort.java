package com.rutaaprendizaje.webflux.domain.ports.in;

import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapabilityTechnologyServicePort {
    Mono<CapabilityWithTechnologiesModel> saveAll(Mono<LinkedCapabilityTechnologyModel> linkModel);

    Mono<CapabilityWithTechnologiesModel> findAllByCapabilityId(Long capabilityId);

    Flux<CapabilityWithTechnologiesModel> findAllByTechnologyAmount(int page, int size, String order);
}
