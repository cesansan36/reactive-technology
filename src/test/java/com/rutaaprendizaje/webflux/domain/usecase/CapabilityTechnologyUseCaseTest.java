package com.rutaaprendizaje.webflux.domain.usecase;

import com.rutaaprendizaje.webflux.domain.model.CapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;
import com.rutaaprendizaje.webflux.domain.model.LinkedCapabilityTechnologyModel;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.out.ICapabilityTechnologyPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityTechnologyUseCaseTest {

    @Mock
    private ITechnologyServicePort technologyServicePort;
    @Mock
    private ICapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort;

    @InjectMocks
    private CapabilityTechnologyUseCase capabilityTechnologyUseCase;

    @Test
    void saveAll_ShouldSaveCapabilitiesAndReturnModel() {
        // Arrange
        LinkedCapabilityTechnologyModel linkedModel = new LinkedCapabilityTechnologyModel(1L, List.of("Java", "Spring"));
        linkedModel.setId(1L);
        linkedModel.setTechnologiesNames(Arrays.asList("Java", "Spring"));

        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Java description");
        TechnologyModel technology2 = new TechnologyModel(2L, "Spring", "Spring description");

        when(technologyServicePort.findAllByNames(any())).thenReturn(Flux.just(technology1, technology2));
        when(capabilityTechnologyPersistencePort.saveAll(any())).thenReturn(Flux.empty());

        // Act
        Mono<CapabilityWithTechnologiesModel> result = capabilityTechnologyUseCase.saveAll(Mono.just(linkedModel));

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(model ->
                        model.getCapabilityId().equals(1L) &&
                                model.getTechnologies().size() == 2 &&
                                model.getTechnologies().get(0).getId().equals(1L) &&
                                model.getTechnologies().get(1).getId().equals(2L))
                .verifyComplete();

        verify(technologyServicePort, times(1)).findAllByNames(any());
        verify(capabilityTechnologyPersistencePort, times(1)).saveAll(any());
    }

    @Test
    void findAllByBootcampId_ShouldReturnBootcampWithCapabilities() {
        // Arrange
        Long capabilityId = 1L;
        CapabilityTechnologyModel relation1 = new CapabilityTechnologyModel(1L, capabilityId, 1L);
        CapabilityTechnologyModel relation2 = new CapabilityTechnologyModel(2L, capabilityId, 2L);

        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Java description");
        TechnologyModel technology2 = new TechnologyModel(2L, "Spring", "Spring description");

        when(capabilityTechnologyPersistencePort.findAllByCapabilityId(anyLong())).thenReturn(Flux.just(relation1, relation2));
        when(technologyServicePort.findAllByIds(anyList())).thenReturn(Flux.just(technology1, technology2));

        // Act
        Mono<CapabilityWithTechnologiesModel> result = capabilityTechnologyUseCase.findAllByCapabilityId(capabilityId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(model ->
                        model.getCapabilityId().equals(capabilityId) &&
                                model.getTechnologies().size() == 2 &&
                                model.getTechnologies().get(0).getId().equals(1L) &&
                                model.getTechnologies().get(1).getId().equals(2L))
                .verifyComplete();

        verify(capabilityTechnologyPersistencePort, times(1)).findAllByCapabilityId(anyLong());
        verify(technologyServicePort, times(1)).findAllByIds(anyList());
    }

    @Test
    void findAllByCapabilityAmount() {
        int page = 0;
        int size = 2;
        String direction = "ASC";

        List<Long> capabilitiesIds = List.of(1L, 2L);
        Flux<Long> capabilitiesIdsFlux = Flux.fromIterable(capabilitiesIds);

        CapabilityTechnologyModel relation1 = new CapabilityTechnologyModel(1L, 1L, 1L);
        Flux<CapabilityTechnologyModel> relationsFlux1 = Flux.just(relation1);
        CapabilityTechnologyModel relation2 = new CapabilityTechnologyModel(2L, 2L, 2L);
        Flux<CapabilityTechnologyModel> relationsFlux2 = Flux.just(relation2);

        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Java description");
        Flux<TechnologyModel> capabilitiesFlux1 = Flux.just(technology1);
        TechnologyModel technology2 = new TechnologyModel(2L, "Spring", "Spring description");
        Flux<TechnologyModel> capabilitiesFlux2 = Flux.just(technology2);

        when(capabilityTechnologyPersistencePort.findPaginatedCapabilityIdsByTechnologyAmount(anyInt(), anyInt(), anyString())).thenReturn(capabilitiesIdsFlux);
        when(capabilityTechnologyPersistencePort.findAllByCapabilityId(anyLong()))
                .thenAnswer(invocation -> {
                    Long capabilityId = invocation.getArgument(0);
                    if (capabilityId == 1L) {
                        return relationsFlux1;
                    } else {
                        return relationsFlux2;
                    }
                });
        when(technologyServicePort.findAllByIds(anyList())).thenReturn(capabilitiesFlux1).thenReturn(capabilitiesFlux2);

        Flux<CapabilityWithTechnologiesModel> resultFlux = capabilityTechnologyUseCase.findAllByTechnologyAmount(page, size, direction);

        StepVerifier.create(resultFlux)
                .expectNextMatches(bootcamp ->
                        bootcamp.getCapabilityId().equals(1L) &&
                                bootcamp.getTechnologies().size() == 1 &&
                                bootcamp.getTechnologies().get(0).getName().equals("Java"))
                .expectNextMatches(bootcamp ->
                        bootcamp.getCapabilityId().equals(2L) &&
                                bootcamp.getTechnologies().size() == 1 &&
                                bootcamp.getTechnologies().get(0).getName().equals("Spring"))
                .verifyComplete();
    }
}