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
        return technologyPersistencePort.findById(id).switchIfEmpty(Mono.error(new TechnologyNotFoundException(TECHNOLOGY_NOT_FOUND)));
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

}
