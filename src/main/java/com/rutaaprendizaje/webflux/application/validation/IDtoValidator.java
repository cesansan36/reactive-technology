package com.rutaaprendizaje.webflux.application.validation;

public interface IDtoValidator {
    <T> T validate(T dto);
}
