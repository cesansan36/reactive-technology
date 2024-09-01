package com.rutaaprendizaje.webflux.configuration.beanconfiguration;

import com.rutaaprendizaje.webflux.application.handler.ICapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.handler.ITechnologyHandler;
import com.rutaaprendizaje.webflux.application.handler.impl.CapabilityTechnologyHandler;
import com.rutaaprendizaje.webflux.application.handler.impl.TechnologyHandler;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ICapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.ITechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.mapper.impl.CapabilityTechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.impl.CapabilityTechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.mapper.impl.TechnologyRequestMapper;
import com.rutaaprendizaje.webflux.application.mapper.impl.TechnologyResponseMapper;
import com.rutaaprendizaje.webflux.application.validation.IDtoValidator;
import com.rutaaprendizaje.webflux.application.validation.impl.DtoValidator;
import com.rutaaprendizaje.webflux.domain.ports.in.ICapabilityTechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.in.ITechnologyServicePort;
import com.rutaaprendizaje.webflux.domain.ports.out.ICapabilityTechnologyPersistencePort;
import com.rutaaprendizaje.webflux.domain.ports.out.ITechnologyPersistencePort;
import com.rutaaprendizaje.webflux.domain.usecase.CapabilityTechnologyUseCase;
import com.rutaaprendizaje.webflux.domain.usecase.TechnologyUseCase;
import com.rutaaprendizaje.webflux.infrastructure.secondary.adapter.CapabilityTechnologyPersistenceAdapter;
import com.rutaaprendizaje.webflux.infrastructure.secondary.adapter.TechnologyPersistenceAdapter;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ICapabilityTechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.ITechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.impl.CapabilityTechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.mapper.impl.TechnologyEntityMapper;
import com.rutaaprendizaje.webflux.infrastructure.secondary.repository.ICapabilityTechnologyRepository;
import com.rutaaprendizaje.webflux.infrastructure.secondary.repository.ITechnologyRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort technologyPersistencePort) {
        return new TechnologyUseCase(technologyPersistencePort);
    }

    @Bean
    public ITechnologyPersistencePort technologyPersistencePort(ITechnologyRepository technologyRepository,
                                                                ITechnologyEntityMapper technologyEntityMapper,
                                                                R2dbcEntityTemplate r2dbcEntityTemplate
    ) {
        return new TechnologyPersistenceAdapter(technologyRepository, technologyEntityMapper, r2dbcEntityTemplate);
    }

    @Bean
    public ITechnologyEntityMapper technologyEntityMapper() {
        return new TechnologyEntityMapper();
    }

    @Bean
    public ITechnologyRequestMapper technologyRequestMapper() {
        return new TechnologyRequestMapper();
    }

    @Bean
    public ITechnologyResponseMapper technologyResponseMapper() {
        return new TechnologyResponseMapper();
    }

    @Bean
    public ITechnologyHandler technologyHandler( ITechnologyServicePort technologyServicePort,
                                                 ITechnologyRequestMapper technologyRequestMapper,
                                                 ITechnologyResponseMapper technologyResponseMapper,
                                                 IDtoValidator dtoValidator) {

        return new TechnologyHandler(technologyServicePort, technologyResponseMapper, technologyRequestMapper, dtoValidator);
    }

    @Bean
    public IDtoValidator dtoValidator(Validator validator) {
        return new DtoValidator(validator);
    }

    @Bean
    public ICapabilityTechnologyEntityMapper capabilityTechnologyEntityMapper() {
        return new CapabilityTechnologyEntityMapper();
    }

    @Bean
    public ICapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort(
            ICapabilityTechnologyRepository capabilityTechnologyRepository,
            ICapabilityTechnologyEntityMapper capabilityTechnologyEntityMapper,
            R2dbcEntityTemplate r2dbcEntityTemplate) {
        return new CapabilityTechnologyPersistenceAdapter(capabilityTechnologyRepository, capabilityTechnologyEntityMapper, r2dbcEntityTemplate);
    }

    @Bean
    public ICapabilityTechnologyServicePort capabilityTechnologyServicePort(ICapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort, ITechnologyServicePort technologyServicePort) {
        return new CapabilityTechnologyUseCase(capabilityTechnologyPersistencePort, technologyServicePort);
    }

    @Bean
    public ICapabilityTechnologyRequestMapper capabilityTechnologyRequestMapper() {
        return new CapabilityTechnologyRequestMapper();
    }

    @Bean
    public ICapabilityTechnologyResponseMapper capabilityTechnologyResponseMapper() {
        return new CapabilityTechnologyResponseMapper();
    }

    @Bean
    public ICapabilityTechnologyHandler capabilityTechnologyHandler(ICapabilityTechnologyServicePort capabilityTechnologyServicePort,
                                                                    ICapabilityTechnologyRequestMapper capabilityTechnologyRequestMapper,
                                                                    ICapabilityTechnologyResponseMapper capabilityTechnologyResponseMapper) {
        return new CapabilityTechnologyHandler(capabilityTechnologyServicePort, capabilityTechnologyRequestMapper, capabilityTechnologyResponseMapper);
    }
}
