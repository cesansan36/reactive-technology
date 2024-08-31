package com.rutaaprendizaje.webflux.infrastructure.secondary.adapter;

import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.out.ITechnologyPersistencePort;
import com.rutaaprendizaje.webflux.infrastructure.secondary.entity.TechnologyEntity;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ITechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class TechnologyPersistenceAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository technologyRepository;
    private final ITechnologyEntityMapper technologyEntityMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<TechnologyModel> findAll() {
        return technologyRepository.findAll().map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Mono<TechnologyModel> findById(Long id) {
        return technologyRepository
                .findById(id)
                .map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Mono<TechnologyModel> findByName(String name) {
        return technologyRepository.findByName(name).map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Mono<TechnologyModel> save(Mono<TechnologyModel> technologyModel) {
        return technologyModel
                .map(technologyEntityMapper::toTechnologyEntity)
                .flatMap(technologyRepository::save)
                .map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Flux<TechnologyModel> findAllPaginated(int page, int size, String sortBy, Sort.Direction direction) {

        Query query = Query.query(Criteria.empty())
                .sort(Sort.by(direction, sortBy))
                .limit(size)
                .offset((long) page * size);

        return r2dbcEntityTemplate.select(query, TechnologyEntity.class)
                .map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Flux<TechnologyModel> findAllByNames(List<String> names) {
        return technologyRepository.findByNameIn(names).map(technologyEntityMapper::toTechnologyModel);
    }

    @Override
    public Flux<TechnologyModel> findAllByIds(List<Long> ids) {
        return technologyRepository.findAllById(ids).map(technologyEntityMapper::toTechnologyModel);
    }
}
