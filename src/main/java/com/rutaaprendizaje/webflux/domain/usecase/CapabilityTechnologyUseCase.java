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

import java.util.ArrayList;
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
//                            .doOnNext(technology -> System.out.println("CapabilityTechnologyUseCase.saveAll() - technology: " + technology.toString()));

                    return technologies.map(technology -> new CapabilityTechnologyModel(null, capabilityId, technology.getId()))
                            .as(capabilityTechnologyPersistencePort::saveAll)
                            .then(Mono.just(buildCapabilityWithTechnologies(capabilityId, technologies)));
                })
                .collectList()
                .map(list -> list.get(0));
    }

    private CapabilityWithTechnologiesModel buildCapabilityWithTechnologies(Long capabilityId, Flux<TechnologyModel> technologies) {

        CapabilityWithTechnologiesModel capabilityWithTechnologiesModel = new CapabilityWithTechnologiesModel(capabilityId, new ArrayList<>());

        technologies
                .collectList()
                .doOnNext(list -> {
                    System.out.println("La lista ======================");
                    list.forEach(technology -> System.out.println("technology: " + technology.toString()));
                })
                .doOnNext(capabilityWithTechnologiesModel::setTechnologies)
                .doOnNext(capa -> System.out.println("CapabilityWithTechnologiesModel: " + capa.toString()));

        return capabilityWithTechnologiesModel;
    }

//    @Override
//    public Mono<List<CapabilityTechnologyModel>> saveAll(Mono<LinkedCapabilityTechnologyModel> linkModel) {
//        return linkModel
//                .flatMapMany(link -> {
//                    Long capabilityId = link.getId();
//
//                    return Mono.just(getTechnologiesNamesFromLinkModel(link))
//                            .flatMapMany(technologyServicePort::findAllByNames)
//                            .map(technology -> new CapabilityTechnologyModel(null, capabilityId, technology.getId()));
//                })
//                .as(capabilityTechnologyPersistencePort::saveAll)
//                .map(capabilityTechnologyModel -> capabilityTechnologyModel)
//                .collectList();
//    }

    private List<String> getTechnologiesNamesFromLinkModel(LinkedCapabilityTechnologyModel linkModel) {
        List<String> technologiesNames;
        technologiesNames = linkModel.getTechnologiesNames();
        return technologiesNames;
    }
}
