package com.rutaaprendizaje.webflux.infrastructure.secondary.exception;

import com.rutaaprendizaje.webflux.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class DuplicateRegistryException extends CustomException {

    public DuplicateRegistryException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
