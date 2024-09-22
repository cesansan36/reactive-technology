package com.rutaaprendizaje.webflux.application.handler.impl;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.application.dto.response.CapabilityWithTechnologiesResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyInCapabilityResponse;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.model.CapabilityWithTechnologiesModel;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import com.rutaaprendizaje.webflux.domain.ports.in.ICapabilityTechnologyServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_CAPABILITY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.LINKED_CAPABILITY_TECHNOLOGIES_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapabilityTechnologyHandlerTest {

    @Mock
    private ICapabilityTechnologyServicePort capabilityTechnologyServicePort;
    @Mock
    private ICapabilityTechnologyRequestMapper capabilityTechnologyRequestMapper;
    @Mock
    private ICapabilityTechnologyResponseMapper capabilityTechnologyResponseMapper;

    @InjectMocks
    private CapabilityTechnologyHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction()).build();
    }

    private RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                /*🥈*/.GET(LINKED_CAPABILITY_TECHNOLOGIES_PATH + GET_BY_CAPABILITY_ID_SUB_PATH, handler::findTechnologiesByCapabilityId)
                /*🥈*/.GET(LINKED_CAPABILITY_TECHNOLOGIES_PATH, handler::findPaginatedCapabilityIdsByTechnologyAmount)
                /*🥈*/.POST(LINKED_CAPABILITY_TECHNOLOGIES_PATH, handler::save)
                .build();
    }

    @Test
    void testSave() {
        LinkCapabilityWithTechnologiesRequest request = new LinkCapabilityWithTechnologiesRequest(1L, List.of("t1", "t2"));
        CapabilityWithTechnologiesModel model = new CapabilityWithTechnologiesModel(
                1L,
                List.of(
                        new TechnologyModel(1L, "t1", ""),
                        new TechnologyModel(2L, "t2", "")));
        CapabilityWithTechnologiesResponse response = new CapabilityWithTechnologiesResponse(
                1L,
                List.of(
                        new TechnologyInCapabilityResponse(1L, "t1"),
                        new TechnologyInCapabilityResponse(2L, "t2")));

        when(capabilityTechnologyServicePort.saveAll(any())).thenReturn(Mono.just(model));
        when(capabilityTechnologyResponseMapper.toCapabilityWithTechnologiesResponse(any(CapabilityWithTechnologiesModel.class))).thenReturn(response);

        webTestClient.post()
                .uri(LINKED_CAPABILITY_TECHNOLOGIES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CapabilityWithTechnologiesResponse.class)
                .value(savedResponse -> assertThat(savedResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindCapabilitiesByBootcampId() {
        Long capabilityId = 1L;
        CapabilityWithTechnologiesModel model = new CapabilityWithTechnologiesModel(1L, new ArrayList<>());
        CapabilityWithTechnologiesResponse response = new CapabilityWithTechnologiesResponse(1L, new ArrayList<>());

        when(capabilityTechnologyServicePort.findAllByCapabilityId(capabilityId))
                .thenReturn(Mono.just(model));
        when(capabilityTechnologyResponseMapper.toCapabilityWithTechnologiesResponse(any()))
                .thenReturn(response);

        webTestClient.get()
                .uri(LINKED_CAPABILITY_TECHNOLOGIES_PATH + "/" + capabilityId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CapabilityWithTechnologiesResponse.class)
                .value(foundResponse -> assertThat(foundResponse).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void testFindPaginatedBootcampByCapabilityAmount_DefaultPagination() {
        List<CapabilityWithTechnologiesModel> capabilityModelList = List.of(new CapabilityWithTechnologiesModel(), new CapabilityWithTechnologiesModel());
        List<CapabilityWithTechnologiesResponse> responseList = List.of(
                new CapabilityWithTechnologiesResponse(1L, List.of(new TechnologyInCapabilityResponse(1L, "t1"))),
                new CapabilityWithTechnologiesResponse(2L, List.of(new TechnologyInCapabilityResponse(2L, "t2"))));

        when(capabilityTechnologyServicePort.findAllByTechnologyAmount(0, 3, "ASC"))
                .thenReturn(Flux.fromIterable(capabilityModelList));

        when(capabilityTechnologyResponseMapper.toCapabilityWithTechnologiesResponse(any()))
                .thenReturn(responseList.get(0), responseList.get(1));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LINKED_CAPABILITY_TECHNOLOGIES_PATH)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityWithTechnologiesResponse.class)
                .value(responses -> {
                    assertThat(responses.size()).isEqualTo(2);
                    assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(responseList.get(0));
                    assertThat(responses.get(1)).usingRecursiveComparison().isEqualTo(responseList.get(1));
                });
    }

    @Test
    void testFindPaginatedBootcampByCapabilityAmount_CustomPaginationAndSorting() {
        List<CapabilityWithTechnologiesModel> capabilityModelList = List.of(new CapabilityWithTechnologiesModel(), new CapabilityWithTechnologiesModel());
        List<CapabilityWithTechnologiesResponse> responseList = List.of(
                new CapabilityWithTechnologiesResponse(1L, List.of(new TechnologyInCapabilityResponse(1L, "t1"))),
                new CapabilityWithTechnologiesResponse(2L, List.of(new TechnologyInCapabilityResponse(2L, "t2"))));

        when(capabilityTechnologyServicePort.findAllByTechnologyAmount(1, 10, "DESC"))
                .thenReturn(Flux.fromIterable(capabilityModelList));

        when(capabilityTechnologyResponseMapper.toCapabilityWithTechnologiesResponse(any()))
                .thenReturn(responseList.get(0), responseList.get(1));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(LINKED_CAPABILITY_TECHNOLOGIES_PATH)
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .queryParam("direction", "DESC")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CapabilityWithTechnologiesResponse.class)
                .value(responses -> {
                    assertThat(responses.size()).isEqualTo(2);
                    assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(responseList.get(0));
                    assertThat(responses.get(1)).usingRecursiveComparison().isEqualTo(responseList.get(1));
                });
    }
}