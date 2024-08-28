package com.rutaaprendizaje.webflux.application.handler.impl;

import com.rutaaprendizaje.webflux.application.dto.request.SaveTechnologyRequest;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.validation.IDtoValidator;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.PAGINATED_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.TECHNOLOGIES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TechnologyHandlerTest {
    @Mock
    private ITechnologyServicePort technologyServicePort;

    @Mock
    private ITechnologyResponseMapper technologyResponseMapper;

    @Mock
    private ITechnologyRequestMapper technologyRequestMapper;

    @Mock
    private IDtoValidator dtoValidator;

    @InjectMocks
    private TechnologyHandler technologyHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction()).build();
    }

    private RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET(TECHNOLOGIES_PATH, technologyHandler::findAll)
                .GET(TECHNOLOGIES_PATH + PAGINATED_SUB_PATH, technologyHandler::findAllPaginated)
                .GET(TECHNOLOGIES_PATH + GET_BY_ID_SUB_PATH, technologyHandler::findById)
                .POST(TECHNOLOGIES_PATH, technologyHandler::save)
                .build();
    }

    @Test
    void testFindAll() {
        TechnologyResponse response1 = new TechnologyResponse(1L, "Tech 1", "Description 1");
        TechnologyResponse response2 = new TechnologyResponse(2L, "Tech 2", "Description 2");

        when(technologyServicePort.findAll()).thenReturn(Flux.just(new TechnologyModel(1L, "Tech 1", "Description 1"), new TechnologyModel(2L, "Tech 2", "Description 2")));
        when(technologyResponseMapper.toTechnologyResponse(any(TechnologyModel.class))).thenReturn(response1, response2);

        webTestClient.get()
                .uri("/technologies")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TechnologyResponse.class)
                .hasSize(2)
                .contains(response1, response2);
    }

    @Test
    void testFindById() {
        TechnologyResponse response = new TechnologyResponse(1L, "Tech 1", "Description 1");

        when(technologyServicePort.findById(1L)).thenReturn(Mono.just(new TechnologyModel(1L, "Tech 1", "Description 1")));
        when(technologyResponseMapper.toTechnologyResponse(any(TechnologyModel.class))).thenReturn(response);

//        webTestClient.get()
//                .uri("/technologies/1")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1)
//                .jsonPath("$.name").isEqualTo("Tech 1")
//                .jsonPath("$.description").isEqualTo("Description 1");

        webTestClient.get()
                .uri("/technologies/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TechnologyResponse.class)
//                .consumeWith(technologyResponse -> {
//                    System.out.println("Response body: " + Objects.requireNonNull(technologyResponse.getResponseBody()).toString());
//                })
                .value(technologyResponse -> assertThat(technologyResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testSave() {
        SaveTechnologyRequest saveRequest = new SaveTechnologyRequest("Tech 1", "Description 1");
        TechnologyResponse response = new TechnologyResponse(1L, "Tech 1", "Description 1");

        when(technologyRequestMapper.toTechnologyModel(any(SaveTechnologyRequest.class))).thenReturn(new TechnologyModel(null, "Tech 1", "Description 1"));
        when(technologyServicePort.save(any(Mono.class))).thenReturn(Mono.just(new TechnologyModel(1L, "Tech 1", "Description 1")));
        when(technologyResponseMapper.toTechnologyResponse(any(TechnologyModel.class))).thenReturn(response);

        webTestClient.post()
                .uri("/technologies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(saveRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TechnologyResponse.class)
                .value(technologyResponse -> assertThat(technologyResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindAllPaginated() {
        TechnologyResponse response1 = new TechnologyResponse(1L, "Tech 1", "Description 1");
        TechnologyResponse response2 = new TechnologyResponse(2L, "Tech 2", "Description 2");

        when(technologyServicePort.findAllPaginated(0, 5, "name", Sort.Direction.ASC))
                .thenReturn(Flux.just(new TechnologyModel(1L, "Tech 1", "Description 1"), new TechnologyModel(2L, "Tech 2", "Description 2")));
        when(technologyResponseMapper.toTechnologyResponse(any(TechnologyModel.class))).thenReturn(response1, response2);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/technologies/paginated")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .queryParam("sortBy", "name")
                        .queryParam("direction", "ASC")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TechnologyResponse.class)
                .hasSize(2)
                .contains(response1, response2);
    }
}