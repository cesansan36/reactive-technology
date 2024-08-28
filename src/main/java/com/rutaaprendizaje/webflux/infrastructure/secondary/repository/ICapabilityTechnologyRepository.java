package com.rutaaprendizaje.webflux.infrastructure.secondary.repository;

import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.CapabilityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICapabilityTechnologyRepository extends ReactiveCrudRepository<CapabilityTechnologyEntity, Long> {
}
