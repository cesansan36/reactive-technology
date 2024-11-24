package com.rutaaprendizaje.webflux.application.dto.request;

import java.util.List;

public record LinkCapabilityWithTechnologiesRequest(
        Long capabilityId,
        List<String> technologiesNames
) {}
