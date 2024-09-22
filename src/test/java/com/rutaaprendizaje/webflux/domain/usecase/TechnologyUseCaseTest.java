package com.rutaaprendizaje.webflux.domain.usecase;

import com.rutaaprendizaje.webflux.domain.exception.TechnologyAlreadyExistsException;
import com.rutaaprendizaje.webflux.domain.exception.TechnologyNotFoundException;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.out.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnFluxOfTechnologyModels() {
        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Backend");
        TechnologyModel technology2 = new TechnologyModel(2L, "Python", "Not java");

        when(technologyPersistencePort.findAll()).thenReturn(Flux.just(technology1, technology2));

        Flux<TechnologyModel> result = technologyUseCase.findAll();

        StepVerifier.create(result)
                .expectNext(technology1)
                .expectNext(technology2)
                .verifyComplete();

        verify(technologyPersistencePort, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnTechnologyModel_WhenFound() {
        Long id = 1L;
        TechnologyModel technology = new TechnologyModel(id, "Java", "Backend");

        when(technologyPersistencePort.findById(id)).thenReturn(Mono.just(technology));

        Mono<TechnologyModel> result = technologyUseCase.findById(id);

        StepVerifier.create(result)
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort, times(1)).findById(id);
    }

    @Test
    void findById_ShouldThrowTechnologyNotFoundException_WhenNotFound() {
        Long id = 1L;

        when(technologyPersistencePort.findById(id)).thenReturn(Mono.empty());

        Mono<TechnologyModel> result = technologyUseCase.findById(id);

        StepVerifier.create(result)
                .expectErrorSatisfies(throwable ->
                        assertThrows(TechnologyNotFoundException.class, () -> { throw throwable; }))
                .verify();

        verify(technologyPersistencePort, times(1)).findById(id);
    }

    @Test
    void save_ShouldSaveTechnologyModel_WhenNotExists() {
        TechnologyModel technology = new TechnologyModel(null, "Java", "Backend");

        when(technologyPersistencePort.findByName(technology.getName())).thenReturn(Mono.empty());
        when(technologyPersistencePort.save(any(Mono.class))).thenReturn(Mono.just(technology));

        Mono<TechnologyModel> result = technologyUseCase.save(Mono.just(technology));

        StepVerifier.create(result)
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort, times(1)).findByName(technology.getName());
        verify(technologyPersistencePort, times(1)).save(any(Mono.class));
    }

    @Test
    void save_ShouldThrowTechnologyAlreadyExistsException_WhenTechnologyExists() {
        TechnologyModel technology = new TechnologyModel(null, "Java", "Backend");

        when(technologyPersistencePort.findByName(technology.getName())).thenReturn(Mono.just(technology));

        Mono<TechnologyModel> result = technologyUseCase.save(Mono.just(technology));

        StepVerifier.create(result)
                .expectError(TechnologyAlreadyExistsException.class)
                .verify();

        verify(technologyPersistencePort, times(1)).findByName(technology.getName());
        verify(technologyPersistencePort, never()).save(any(Mono.class));
    }

    @Test
    void findAllPaginated_ShouldReturnPaginatedTechnologyModels() {
        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Backend");
        TechnologyModel technology2 = new TechnologyModel(2L, "Python", "Not java");

        when(technologyPersistencePort.findAllPaginated(0, 2, "name", Sort.Direction.ASC))
                .thenReturn(Flux.just(technology1, technology2));

        Flux<TechnologyModel> result = technologyUseCase.findAllPaginated(0, 2, "name", Sort.Direction.ASC);

        StepVerifier.create(result)
                .expectNext(technology1)
                .expectNext(technology2)
                .verifyComplete();

        verify(technologyPersistencePort, times(1)).findAllPaginated(0, 2, "name", Sort.Direction.ASC);
    }

    @Test
    void findAllByNames_Success() {
        // Given
        List<String> names = List.of("Java", "Python");

        TechnologyModel technology1 = new TechnologyModel(1L, "Java", "Java description");
        TechnologyModel technology2 = new TechnologyModel(2L, "Python", "Python description");

        when(technologyPersistencePort.findAllByNames(names))
                .thenReturn(Flux.just(technology1, technology2));

        // When
        Flux<TechnologyModel> result = technologyUseCase.findAllByNames(names);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(tech ->
                        tech.getId().equals(1L) &&
                                tech.getName().equals("Java") &&
                                tech.getDescription().equals("Java description")
                )
                .expectNextMatches(tech ->
                        tech.getId().equals(2L) &&
                                tech.getName().equals("Python") &&
                                tech.getDescription().equals("Python description")
                )
                .verifyComplete();
    }

    @Test
    void findAllByNames_TechnologyNotFoundException() {
        List<String> names = List.of("Java", "Python");

        TechnologyModel technology = new TechnologyModel(1L,"Java","Coffee");
        Flux<TechnologyModel> technologiesFoundFlux = Flux.just(technology);

        when(technologyPersistencePort.findAllByNames(names)).thenReturn(technologiesFoundFlux);

        Flux<TechnologyModel> result = technologyUseCase.findAllByNames(names);

        StepVerifier.create(result)
                .expectError(TechnologyNotFoundException.class)
                .verify();
    }

    @Test
    void findAllByNames_EmptyNamesList() {
        // Given
        List<String> names = List.of();

        when(technologyPersistencePort.findAllByNames(names))
                .thenReturn(Flux.empty());

        // When
        Flux<TechnologyModel> result = technologyUseCase.findAllByNames(names);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAllByIds() {
        // Datos de prueba
        List<Long> ids = Arrays.asList(1L, 2L);

        TechnologyModel technology1 = new TechnologyModel(1L, "Technology 1", "Description 1");
        TechnologyModel technology2 = new TechnologyModel(2L, "Technology 2", "Description 2");
        Flux<TechnologyModel> capabilitiesWithNoTechnologies = Flux.just(technology1, technology2);

        when(technologyPersistencePort.findAllByIds(ids)).thenReturn(capabilitiesWithNoTechnologies);

        // Ejecución y verificación
        StepVerifier.create(technologyUseCase.findAllByIds(ids))
                .expectNextMatches(technology -> technology.getId().equals(1L) && technology.getDescription().equals("Description 1"))
                .expectNextMatches(technology -> technology.getId().equals(2L) && technology.getDescription().equals("Description 2"))
                .verifyComplete();
    }
}