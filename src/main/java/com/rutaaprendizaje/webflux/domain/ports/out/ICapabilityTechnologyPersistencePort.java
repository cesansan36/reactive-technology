package com.rutaaprendizaje.webflux.domain.ports.out;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import reactor.core.publisher.Flux;

public interface ICapabilityTechnologyPersistencePort {
    Flux<CapabilityTechnologyModel> saveAll(Flux<CapabilityTechnologyModel> capabilityTechnologyModels);

    Flux<CapabilityTechnologyModel> findAllByCapabilityId(Long capabilityId);

    Flux<Long> findPaginatedCapabilityIdsByTechnologyAmount(int page, int size, String order);
}
