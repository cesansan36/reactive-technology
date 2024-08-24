package com.rutaaprendizaje.webflux.domain.exception;

import com.rutaaprendizaje.webflux.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class TechnologyNotFoundException extends CustomException {
    public TechnologyNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
