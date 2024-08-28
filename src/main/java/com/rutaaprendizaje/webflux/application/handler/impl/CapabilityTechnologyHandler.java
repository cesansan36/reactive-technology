package com.rutaaprendizaje.webflux.application.handler.impl;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.application.dto.response.CapabilityWithTechnologiesResponse;
import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.domain.ports.in.ICapabilityTechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
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

}
