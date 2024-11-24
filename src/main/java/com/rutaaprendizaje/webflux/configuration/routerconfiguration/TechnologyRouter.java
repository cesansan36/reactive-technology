package com.rutaaprendizaje.webflux.configuration.routerconfiguration;

import com.rutaaprendizaje.webflux.application.dto.request.LinkCapabilityWithTechnologiesRequest;
import com.rutaaprendizaje.webflux.application.dto.request.SaveTechnologyRequest;
import com.rutaaprendizaje.webflux.application.dto.response.CapabilityWithTechnologiesResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyForCapabilityResponse;
import com.rutaaprendizaje.webflux.application.dto.response.TechnologyResponse;
import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.handler.ITechnologyHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

import static com.rutaaprendizaje.webflux.util.Constants.BY_NAMES_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_CAPABILITY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.GET_BY_ID_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.LINKED_CAPABILITY_TECHNOLOGIES_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.PAGINATED_SUB_PATH;
import static com.rutaaprendizaje.webflux.util.Constants.TECHNOLOGIES_PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
public class TechnologyRouter {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> router(ITechnologyHandler technologyHandler, ICapabilityTechnologyHandler capabilityTechnologyHandler) {
        return route()
                /*💤*/.GET(TECHNOLOGIES_PATH, technologyHandler::findAll, getAllConsumer())
                /*💤*/.GET(TECHNOLOGIES_PATH + GET_BY_ID_SUB_PATH, technologyHandler::findById, findByIdConsumer())
                /*💤*/.GET(TECHNOLOGIES_PATH + BY_NAMES_SUB_PATH, technologyHandler::findByNames, findByNamesConsumer())
                /*⭐*/.GET(TECHNOLOGIES_PATH + PAGINATED_SUB_PATH, technologyHandler::findAllPaginated, findAllPaginatedConsumer())
                /*⭐*/.POST(TECHNOLOGIES_PATH, technologyHandler::save, saveConsumer())
                /*🥈*/.GET(LINKED_CAPABILITY_TECHNOLOGIES_PATH + GET_BY_CAPABILITY_ID_SUB_PATH, capabilityTechnologyHandler::findTechnologiesByCapabilityId, findTechnologiesByCapabilityIdConsumer())
                /*🥈*/.GET(LINKED_CAPABILITY_TECHNOLOGIES_PATH, capabilityTechnologyHandler::findPaginatedCapabilityIdsByTechnologyAmount, findPaginatedCapabilityIdsByTechnologyAmountConsumer())
                /*🥈*/.POST(LINKED_CAPABILITY_TECHNOLOGIES_PATH, capabilityTechnologyHandler::save, saveCapabilityTechnologyRelationConsumer())
                .build();
    }

    private Consumer<Builder> getAllConsumer() {
        return builder -> builder
                .operationId("GetAllTechnologies")
                .summary("💤 Get all technologies")
                .tag("💤 Experimentación")
                .response(responseBuilder().responseCode("200").description("Get all technologies").implementationArray(TechnologyResponse.class));
    }

    private Consumer<Builder> findByIdConsumer() {
        return builder -> builder
                .operationId("GetTechnologyById")
                .summary("💤 Get technology by id")
                .tag("💤 Experimentación")
                .response(responseBuilder().responseCode("200").description("Get technology by id").implementation(TechnologyResponse.class));
    }

    private Consumer<Builder> findByNamesConsumer() {
        return builder -> builder
                .operationId("GetTechnologyByNames")
                .summary("💤 Get technologies by names")
                .tag("💤 Experimentación")
                .response(responseBuilder().responseCode("200").description("Get technologies by names").implementationArray(TechnologyForCapabilityResponse.class));
    }

    private Consumer<Builder> findAllPaginatedConsumer() {
        return builder -> builder
                .operationId("GetAllPaginatedTechnologies")
                .summary("⭐ Get all paginated technologies")
                .tag("⭐ Solo tecnologías")
                .parameter(parameterBuilder().name("page").description("Page number").in(ParameterIn.QUERY).required(true).example("0"))
                .parameter(parameterBuilder().name("size").description("Page size").in(ParameterIn.QUERY).required(true).example("3"))
                .parameter(parameterBuilder().name("sortBy").description("Sort by").in(ParameterIn.QUERY).required(true).example("name"))
                .parameter(parameterBuilder().name("direction").description("Sort direction").in(ParameterIn.QUERY).required(true).example("ASC"))
                .response(responseBuilder().responseCode("200").description("Get all paginated technologies").implementationArray(TechnologyResponse.class));
    }

    private Consumer<Builder> saveConsumer() {
        return builder -> builder
                .operationId("SaveTechnology")
                .summary("⭐ Save technology")
                .tag("⭐ Solo tecnologías")
                .requestBody(requestBodyBuilder().implementation(SaveTechnologyRequest.class))
                .response(responseBuilder().responseCode("200").description("Saved technology").implementation(TechnologyResponse.class));
    }

    private Consumer<Builder> findTechnologiesByCapabilityIdConsumer() {
        return builder -> builder
                .operationId("GetTechnologiesByCapabilityId")
                .summary("🥈 Get technologies by capability id")
                .tag("🥈 Relación entre Capacidad y Tecnología")
                .parameter(parameterBuilder().name("capabilityId").description("Id of the capability").in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200").description("Get technologies by capability id").implementation(CapabilityWithTechnologiesResponse.class));
    }

    private Consumer<Builder> findPaginatedCapabilityIdsByTechnologyAmountConsumer() {
        return builder -> builder
                .operationId("GetPaginatedCapabilityIdsByTechnologyAmount")
                .summary("🥈 Get paginated capability ids by technology amount")
                .tag("🥈 Relación entre Capacidad y Tecnología")
                .parameter(parameterBuilder().name("page").description("Page number").in(ParameterIn.QUERY).required(true).example("0"))
                .parameter(parameterBuilder().name("size").description("Page size").in(ParameterIn.QUERY).required(true).example("3"))
                .parameter(parameterBuilder().name("direction").description("Sort direction").in(ParameterIn.QUERY).required(true).example("ASC"))
                .response(responseBuilder().responseCode("200").description("OK").implementationArray(CapabilityWithTechnologiesResponse.class));
    }

    private Consumer<Builder> saveCapabilityTechnologyRelationConsumer() {
        return builder -> builder
                .operationId("SaveCapabilityTechnologyRelation")
                .summary("🥈 Save capability technology relation")
                .tag("🥈 Relación entre Capacidad y Tecnología")
                .requestBody(requestBodyBuilder().implementation(LinkCapabilityWithTechnologiesRequest.class))
                .response(responseBuilder().responseCode("200").description("Saved capability technology relation").implementation(CapabilityWithTechnologiesResponse.class));
    }
}
