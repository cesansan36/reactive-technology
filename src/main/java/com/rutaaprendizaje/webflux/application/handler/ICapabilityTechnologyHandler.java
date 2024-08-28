package com.rutaaprendizaje.webflux.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ICapabilityTechnologyHandler {
    Mono<ServerResponse> save(ServerRequest request);
}
