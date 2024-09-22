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

                    Flux<TechnologyModel> technologies = Mono.just(getTechnologiesNamesFromLinkModel(link))
                            .flatMapMany(technologyServicePort::findAllByNames);

                    return technologies
                            .map(technology -> new CapabilityTechnologyModel(null, capabilityId, technology.getId()))
                            .as(capabilityTechnologyPersistencePort::saveAll)
                            .thenMany(technologies.collectList().map(list -> buildCapabilityWithTechnologies(capabilityId, list)));
                })
                .next();
    }

    @Override
    public Mono<CapabilityWithTechnologiesModel> findAllByCapabilityId(Long capabilityId) {
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

    @Override
    public Flux<CapabilityWithTechnologiesModel> findAllByTechnologyAmount(int page, int size, String order) {

        return capabilityTechnologyPersistencePort.findPaginatedCapabilityIdsByTechnologyAmount(page, size, order)
                .flatMap(this::findAllByCapabilityId);
    }

    private CapabilityWithTechnologiesModel buildCapabilityWithTechnologies(Long capabilityId, List<TechnologyModel> technologies) {
        return new CapabilityWithTechnologiesModel(capabilityId, technologies);
    }

    private List<String> getTechnologiesNamesFromLinkModel(LinkedCapabilityTechnologyModel linkModel) {
        List<String> technologiesNames;
        technologiesNames = linkModel.getTechnologiesNames();
        return technologiesNames;
    }

}
