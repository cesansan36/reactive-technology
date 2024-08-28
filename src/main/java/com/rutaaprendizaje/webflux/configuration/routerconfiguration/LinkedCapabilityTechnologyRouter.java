//package com.rutaaprendizaje.webflux.configuration.routerconfiguration;
//
//import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import static com.rutaaprendizaje.webflux.util.Constants.LINKED_CAPABILITY_TECHNOLOGIES_PATH;
//
//@Configuration
//public class LinkedCapabilityTechnologyRouter {
//
//    @Bean
//    public RouterFunction<ServerResponse> linkedCapabilityTechnologyRouter(ICapabilityTechnologyHandler capabilityTechnologyHandler) {
//        return RouterFunctions.route()
//                .POST(LINKED_CAPABILITY_TECHNOLOGIES_PATH, capabilityTechnologyHandler::save)
//                .build();
//    }
//}
