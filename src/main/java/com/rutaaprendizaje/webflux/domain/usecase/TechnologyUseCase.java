package com.rutaaprendizaje.webflux.domain.usecase;

import com.rutaaprendizaje.webflux.domain.exception.TechnologyAlreadyExistsException;
import com.rutaaprendizaje.webflux.domain.exception.TechnologyNotFoundException;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.out.ITechnologyPersistencePort;
import com.rutaaprendizaje.webflux.domain.util.TechnologyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.AT_LEAST_ONE_TECHNOLOGY_NOT_FOUND;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.TECHNOLOGY_ALREADY_EXISTS;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.TECHNOLOGY_NOT_FOUND;

@RequiredArgsConstructor
public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;

    @Override
    public Flux<TechnologyModel> findAll() {
        return technologyPersistencePort.findAll();
    }

    @Override
    public Mono<TechnologyModel> findById(Long id) {
        return technologyPersistencePort
                .findById(id)
                .switchIfEmpty(Mono.error(new TechnologyNotFoundException(TECHNOLOGY_NOT_FOUND)));
    }

    @Override
    public Mono<TechnologyModel> save(Mono<TechnologyModel> technologyModel) {

        return technologyModel
                .map(TechnologyValidator::validateTechnologyToSave)
                .flatMap(model -> technologyPersistencePort.findByName(model.getName())
                        .hasElement()
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                                ? Mono.error(new TechnologyAlreadyExistsException(TECHNOLOGY_ALREADY_EXISTS))
                                : technologyPersistencePort.save(Mono.just(model))
                        )
                );
    }

    @Override
    public Flux<TechnologyModel> findAllPaginated(int page, int size, String sortBy, Sort.Direction direction) {
        return technologyPersistencePort.findAllPaginated(page, size, sortBy, direction);
    }

    @Override
    public Flux<TechnologyModel> findAllByNames(List<String> names) {
        return technologyPersistencePort.findAllByNames(names)
                .collectList()
                .flatMapMany(technologies -> {
                    if (technologies.size() != names.size()) {
                        return Mono.error(new TechnologyNotFoundException(AT_LEAST_ONE_TECHNOLOGY_NOT_FOUND));
                    }
                    return Flux.fromIterable(technologies);
                });
    }

    @Override
    public Flux<TechnologyModel> findAllByIds(List<Long> ids) {
        return technologyPersistencePort.findAllByIds(ids);
    }
}
