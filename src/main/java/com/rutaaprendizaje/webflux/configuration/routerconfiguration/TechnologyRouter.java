package com.rutaaprendizaje.webflux.configuration.routerconfiguration;

import com.rutaaprendizaje.webflux.application.handler.ITechnologyHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.PAGINATED_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.PATH;

@Configuration
public class TechnologyRouter {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(ITechnologyHandler technologyHandler) {
        return RouterFunctions.route()
                .GET(PATH, technologyHandler::findAll)
                .GET(PATH + PAGINATED_SUB_PATH, technologyHandler::findAllPaginated)
                .GET(PATH + GET_BY_ID_SUB_PATH, technologyHandler::findById)
                .POST(PATH, technologyHandler::save)
                .build();
    }
}
