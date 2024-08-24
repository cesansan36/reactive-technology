package com.rutaaprendizaje.webflux.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ITechnologyHandler {

    Mono<ServerResponse> findAll(ServerRequest request);

    Mono<ServerResponse> findById(ServerRequest request);

    Mono<ServerResponse> save(ServerRequest request);

    Mono<ServerResponse> findAllPaginated(ServerRequest request);
}
