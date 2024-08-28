package com.rutaaprendizaje.webflux.infrastructure.secondary.adapter;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.out.ICapabilityTechnologyPersistencePort;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ICapabilityTechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.repository.ICapabilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CapabilityTechnologyPersistenceAdapter implements ICapabilityTechnologyPersistencePort {

    private final ICapabilityTechnologyRepository capabilityTechnologyRepository;
    private final ICapabilityTechnologyEntityMapper capabilityTechnologyEntityMapper;

    @Override
    @Transactional
    public Flux<CapabilityTechnologyModel> saveAll(Flux<CapabilityTechnologyModel> capabilityTechnologyModels) {
        return capabilityTechnologyModels
                .map(capabilityTechnologyEntityMapper::toEntity)
                .collectList() // Convertimos a lista para asegurar que todos los elementos se procesan juntos
                .flatMapMany(capabilityTechnologyEntities ->
                        capabilityTechnologyRepository
                                .saveAll(capabilityTechnologyEntities)
                                .map(capabilityTechnologyEntityMapper::toModel)
                );
    }
}
