package com.rutaaprendizaje.webflux.application.handler.impl;

import com.rutaaprendizaje.webflux.application.dto.request.SaveTechnologyRequest;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyForCapabilityResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.application.handler.ITechnologyHandler;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.validation.IDtoValidator;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyResponseMapper technologyResponseMapper;
    private final ITechnologyRequestMapper technologyRequestMapper;
    private final IDtoValidator dtoValidator;

    private static final List<String> ALLOWED_ORDER_BY_VALUES = List.of("id", "name", "description");

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<TechnologyResponse> technologyResponseFlux = technologyServicePort
                .findAll()
                .map(technologyResponseMapper::toTechnologyResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(technologyResponseFlux, TechnologyResponse.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));

        Mono<TechnologyResponse> technologyResponseMono = technologyServicePort.findById(id).map(technologyResponseMapper::toTechnologyResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(technologyResponseMono, TechnologyResponse.class);
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<SaveTechnologyRequest> requestMono = request.bodyToMono(SaveTechnologyRequest.class)
                .flatMap((dto -> {
                    try {
                        dtoValidator.validate(dto);
                        return Mono.just(dto);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                }));

        Mono<TechnologyResponse> responseMono = requestMono.map(technologyRequestMapper::toTechnologyModel)
                .flatMap(technologyModel -> technologyServicePort.save(Mono.just(technologyModel)))
                .map(technologyResponseMapper::toTechnologyResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseMono, TechnologyResponse.class);
    }

    @Override
    public Mono<ServerResponse> findAllPaginated(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("5"));
        String sortBy = request.queryParam("sortBy").filter(ALLOWED_ORDER_BY_VALUES::contains).orElse("name");
        Sort.Direction direction = Sort.Direction.fromString(request.queryParam("direction").orElse("ASC"));

        Flux<TechnologyResponse> technologyResponseFlux = technologyServicePort.findAllPaginated(page, size, sortBy, direction)
                .map(technologyResponseMapper::toTechnologyResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(technologyResponseFlux, TechnologyResponse.class);
    }

    @Override
    public Mono<ServerResponse> findByNames(ServerRequest request) {
        List<String> names = request.queryParams().get("names");

        Flux<TechnologyForCapabilityResponse> technologyResponseFlux = technologyServicePort.findAllByNames(names)
                .map(technologyResponseMapper::toTechnologyForCapabilityResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(technologyResponseFlux, TechnologyForCapabilityResponse.class);
    }

}
