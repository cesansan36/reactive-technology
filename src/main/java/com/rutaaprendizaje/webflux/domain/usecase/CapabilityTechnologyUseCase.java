package com.rutaaprendizaje.webflux.domain.usecase;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.in.ICapabilityTechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.out.ICapabilityTechnologyPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapabilityTechnologyUseCase implements ICapabilityTechnologyServicePort {

    private final ICapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort;
    private final ITechnologyServicePort technologyServicePort;

    public CapabilityTechnologyUseCase(ICapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort, ITechnologyServicePort technologyServicePort) {
        this.capabilityTechnologyPersistencePort = capabilityTechnologyPersistencePort;
        this.technologyServicePort = technologyServicePort;
    }

    @Override
    public Mono<CapabilityWithTechnologiesModel> saveAll(Mono<LinkedCapabilityTechnologyModel> linkModel) {
        return linkModel
                .flatMapMany(link -> {
                    Long capabilityId = link.getId();

                    // Obtenemos las tecnologías desde el servicio
                    Flux<TechnologyModel> technologies = Mono.just(getTechnologiesNamesFromLinkModel(link))
                            .flatMapMany(technologyServicePort::findAllByNames);

                    // Guardamos las asociaciones en la base de datos
                    return technologies
                            .map(technology -> new CapabilityTechnologyModel(null, capabilityId, technology.getId()))
                            .as(capabilityTechnologyPersistencePort::saveAll)
                            // Devolvemos el CapabilityWithTechnologiesModel con la lista de tecnologías
                            .thenMany(technologies.collectList().map(list -> buildCapabilityWithTechnologies(capabilityId, list)));
                })
                .next();
    }

    @Override
    public Mono<CapabilityWithTechnologiesModel> findAllByCapabilityId(Long capabilityId) {
                // Busca los capability_technology por su capability_id
        return capabilityTechnologyPersistencePort
                .findAllByCapabilityId(capabilityId)
                // Extrae de los capability_technology el technology_id
                .map(CapabilityTechnologyModel::getTechnologyId)
                .collectList()
                // Busca las tecnologías por su id
                .flatMapMany(technologyServicePort::findAllByIds)
                // Crea un CapabilityWithTechnologiesModel combinando las tecnologías obtenidas y el capability_id inicial
                .collectList()
                .map(technologyList -> {
                    CapabilityWithTechnologiesModel capabilityWithTechnologies = new CapabilityWithTechnologiesModel();
                    capabilityWithTechnologies.setCapabilityId(capabilityId);
                    capabilityWithTechnologies.setTechnologies(technologyList);
                    return capabilityWithTechnologies;
                });
    }

    @Override
    public Flux<CapabilityWithTechnologiesModel> findAllByTechnologyAmount(int page, int size, String order) {
        Flux<Long> capabilityIds = capabilityTechnologyPersistencePort.findPaginatedCapabilityIdsByTechnologyAmount(page, size, order);

        Flux<CapabilityWithTechnologiesModel> capabilities = capabilityIds
                .flatMap(capabilityId -> {
                    return findAllByCapabilityId(capabilityId);
                });

        return capabilities;
    }


    private CapabilityWithTechnologiesModel buildCapabilityWithTechnologies(Long capabilityId, List<TechnologyModel> technologies) {
        CapabilityWithTechnologiesModel capabilityWithTechnologiesModel = new CapabilityWithTechnologiesModel(capabilityId, technologies);
        technologies.forEach(technology -> System.out.println("technology: " + technology.toString())); // Para verificar los resultados
        return capabilityWithTechnologiesModel;
    }

    private List<String> getTechnologiesNamesFromLinkModel(LinkedCapabilityTechnologyModel linkModel) {
        List<String> technologiesNames;
        technologiesNames = linkModel.getTechnologiesNames();
        return technologiesNames;
    }

    // ❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕
    // Los métodos listados abajo son solo para propósitos de aprendizaje, no son necesarios para el correcto funcionamiento de la aplicación
    // ❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕❕
    public Mono<CapabilityWithTechnologiesModel> findAllByCapabilityIdNonCompact(Long capabilityId) {

        Flux<CapabilityTechnologyModel> capabilityTechnologyFlux = capabilityTechnologyPersistencePort
                .findAllByCapabilityId(capabilityId);

        Mono<List<Long>> technologiesIds = capabilityTechnologyFlux
                .map(capabilityTechnology -> {
                            return capabilityTechnology.getTechnologyId();
                        }
                ).collectList();

        Flux<TechnologyModel> technologyFlux = technologiesIds
                .flatMapMany(ids -> {
                            return technologyServicePort.findAllByIds(ids);
                        }
                );

        Mono<CapabilityWithTechnologiesModel> finalCapabilityWithTechnologiesModelResult = technologyFlux
                .collectList()
                .map(technologyList -> {
                    CapabilityWithTechnologiesModel capabilityWithTechnologies = new CapabilityWithTechnologiesModel();
                    capabilityWithTechnologies.setCapabilityId(capabilityId);
                    capabilityWithTechnologies.setTechnologies(technologyList);
                    return capabilityWithTechnologies;
                });

        return finalCapabilityWithTechnologiesModelResult;
    }

    public Mono<CapabilityWithTechnologiesModel> findAllByCapabilityIdCompact(Long capabilityId) {

        return capabilityTechnologyPersistencePort
                .findAllByCapabilityId(capabilityId)
                .map(CapabilityTechnologyModel::getTechnologyId)
                .collectList()
                .flatMapMany(technologyServicePort::findAllByIds)
                .collectList()
                .map(technologyList -> {
                    CapabilityWithTechnologiesModel capabilityWithTechnologies = new CapabilityWithTechnologiesModel();
                    capabilityWithTechnologies.setCapabilityId(capabilityId);
                    capabilityWithTechnologies.setTechnologies(technologyList);
                    return capabilityWithTechnologies;
                });
    }

    public Mono<CapabilityWithTechnologiesModel> findAllByCapabilityIdSemiCompact(Long capabilityId) {
        return findTechnologiesByCapabilityId(capabilityId)
                .flatMapMany(this::findTechnologiesByIds)  // Usa flatMapMany ya que findTechnologiesByIds retorna un Flux
                .collectList()
                .map(technologyList -> buildFinalCapabilityWithTechnologiesModel(capabilityId, technologyList));
    }

    private Mono<List<Long>> findTechnologiesByCapabilityId(Long capabilityId) {
        return capabilityTechnologyPersistencePort
                .findAllByCapabilityId(capabilityId)
                .map(CapabilityTechnologyModel::getTechnologyId)
                .collectList();
    }

    private Flux<TechnologyModel> findTechnologiesByIds(List<Long> technologyIds) {
        return technologyServicePort.findAllByIds(technologyIds);
    }

    private CapabilityWithTechnologiesModel buildFinalCapabilityWithTechnologiesModel(Long capabilityId, List<TechnologyModel> technologies) {
        CapabilityWithTechnologiesModel capabilityWithTechnologies = new CapabilityWithTechnologiesModel();
        capabilityWithTechnologies.setCapabilityId(capabilityId);
        capabilityWithTechnologies.setTechnologies(technologies);
        return capabilityWithTechnologies;
    }

}
