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
}
