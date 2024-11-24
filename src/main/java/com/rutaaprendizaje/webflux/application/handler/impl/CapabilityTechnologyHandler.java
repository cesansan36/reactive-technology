package com.rutaaprendizaje.webflux.application.handler.impl;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.application.dto.response.CapabilityWithTechnologiesResponse;
import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.ports.in.ICapabilityTechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapabilityTechnologyHandler implements ICapabilityTechnologyHandler {

    private final ICapabilityTechnologyServicePort capabilityTechnologyServicePort;
    private final ICapabilityTechnologyRequestMapper capabilityTechnologyRequestMapper;
    private final ICapabilityTechnologyResponseMapper capabilityTechnologyResponseMapper;

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {

        Mono<CapabilityWithTechnologiesResponse> responseMono = request
                .bodyToMono(LinkCapabilityWithTechnologiesRequest.class)
                .map(capabilityTechnologyRequestMapper::toCapabilityModel)
                .as(capabilityTechnologyServicePort::saveAll)
                .map(capabilityTechnologyResponseMapper::toCapabilityWithTechnologiesResponse);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseMono, CapabilityWithTechnologiesResponse.class);
    }

    @Override
    public Mono<ServerResponse> findTechnologiesByCapabilityId(ServerRequest request) {
        Long capabilityId = Long.valueOf(request.pathVariable("capabilityId"));
        Mono<CapabilityWithTechnologiesResponse> response = capabilityTechnologyServicePort
                .findAllByCapabilityId(capabilityId)
                .map(capabilityTechnologyResponseMapper::toCapabilityWithTechnologiesResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, CapabilityWithTechnologiesResponse.class);
    }

    @Override
    public Mono<ServerResponse> findPaginatedCapabilityIdsByTechnologyAmount(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("3"));
        String order = request.queryParam("direction").orElse(Sort.Direction.ASC.name());

        Flux<CapabilityWithTechnologiesResponse> response = capabilityTechnologyServicePort.findAllByTechnologyAmount(page, size, order)
                .map(capabilityTechnologyResponseMapper::toCapabilityWithTechnologiesResponse);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response, CapabilityWithTechnologiesResponse.class);
    }
}
