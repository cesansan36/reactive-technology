package com.rutaaprendizaje.webflux.infrastructure.secondary.repository;

import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {

    Mono<TechnologyEntity> findByName(String name);
}
