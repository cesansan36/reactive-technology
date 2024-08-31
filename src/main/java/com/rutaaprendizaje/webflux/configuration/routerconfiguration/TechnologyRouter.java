package com.rutaaprendizaje.webflux.configuration.routerconfiguration;

import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.handler.ITechnologyHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.rutaaprendizaje.webflux.util.Constants.BY_NAMES_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_CAPABILITY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.LINKED_CAPABILITY_TECHNOLOGIES_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.PAGINATED_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.TECHNOLOGIES_PATH;

@Configuration
public class TechnologyRouter {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(ITechnologyHandler technologyHandler, ICapabilityTechnologyHandler capabilityTechnologyHandler) {
        return RouterFunctions.route()
                .GET(TECHNOLOGIES_PATH + PAGINATED_SUB_PATH, technologyHandler::findAllPaginated)
                .GET(TECHNOLOGIES_PATH + GET_BY_ID_SUB_PATH, technologyHandler::findById)
                .GET(TECHNOLOGIES_PATH + BY_NAMES_SUB_PATH, technologyHandler::findByNames)
                .GET(LINKED_CAPABILITY_TECHNOLOGIES_PATH + GET_BY_CAPABILITY_ID_SUB_PATH, capabilityTechnologyHandler::findTechnologiesByCapabilityId)
                .GET(TECHNOLOGIES_PATH, technologyHandler::findAll)
                .POST(TECHNOLOGIES_PATH, technologyHandler::save)
                .POST(LINKED_CAPABILITY_TECHNOLOGIES_PATH, capabilityTechnologyHandler::save)
                .build();
    }
}
