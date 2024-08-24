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

}