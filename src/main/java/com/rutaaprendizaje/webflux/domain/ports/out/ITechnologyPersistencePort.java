package com.rutaaprendizaje.webflux.domain.ports.out;

import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Flux<TechnologyModel> findAll();

    Mono<TechnologyModel> findById(Long id);

    Mono<TechnologyModel> findByName(String name);

    Mono<TechnologyModel> save(Mono<TechnologyModel> technologyModel);

    Flux<TechnologyModel> findAllPaginated(int page, int size, String sortBy, Sort.Direction direction);
}
