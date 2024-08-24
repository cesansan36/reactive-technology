package com.rutaaprendizaje.webflux.domain.exception;

import com.rutaaprendizaje.webflux.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class TechnologyAlreadyExistsException extends CustomException {

    public TechnologyAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
