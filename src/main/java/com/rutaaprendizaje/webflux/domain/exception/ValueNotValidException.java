package com.rutaaprendizaje.webflux.domain.exception;

import com.rutaaprendizaje.webflux.configuration.exceptionconfiguration.CustomException;
import org.springframework.http.HttpStatus;

public class ValueNotValidException extends CustomException {

    public ValueNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, "C+: "+message);
    }
}