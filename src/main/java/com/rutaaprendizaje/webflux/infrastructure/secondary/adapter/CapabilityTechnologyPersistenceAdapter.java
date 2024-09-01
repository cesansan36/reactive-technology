package com.rutaaprendizaje.webflux.infrastructure.secondary.adapter;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.out.ICapabilityTechnologyPersistencePort;
import com.rutaaprendizaje.webflux.infrastructure.secondary.exception.DuplicateRegistryException;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ICapabilityTechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.repository.ICapabilityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizaje.webflux.infrastructure.secondary.util.ExceptionConstants.DUPLICATE_RELATIONSHIP_EXCEPTION;

@RequiredArgsConstructor
@Slf4j
public class CapabilityTechnologyPersistenceAdapter implements ICapabilityTechnologyPersistencePort {

    private final ICapabilityTechnologyRepository capabilityTechnologyRepository;
    private final ICapabilityTechnologyEntityMapper capabilityTechnologyEntityMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

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
                                .onErrorResume(DuplicateKeyException.class, ex ->
                                        Mono.error(new DuplicateRegistryException(DUPLICATE_RELATIONSHIP_EXCEPTION))
                                )
                );
    }

    @Override
    public Flux<CapabilityTechnologyModel> findAllByCapabilityId(Long capabilityId) {
        return capabilityTechnologyRepository.findAllByCapabilityId(capabilityId)
                .map(capabilityTechnologyEntityMapper::toModel);
    }

    @Override
    public Flux<Long> findPaginatedCapabilityIdsByTechnologyAmount(int page, int size, String order) {
        // Construye la parte dinámica de la consulta para el orden
        String orderByClause = "ORDER BY COUNT(technology_id) " + (order.equalsIgnoreCase("desc") ? "DESC" : "ASC");

        // Construye la consulta SQL completa
        String query = "SELECT capability_id FROM capability_technology " +
                "GROUP BY capability_id " +
                orderByClause + " " +
                "LIMIT :size OFFSET :offset";

        // Ejecuta la consulta
        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind("size", size)
                .bind("offset", page * size)
                .map(row -> row.get("capability_id", Long.class))
                .all();
    }
}
