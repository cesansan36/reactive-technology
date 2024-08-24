package com.rutaaprendizaje.webflux.application.validation.impl;


import com.rutaaprendizaje.webflux.application.applicationexceptions.ValueNotValidException;
import com.rutaaprendizaje.webflux.application.validation.IDtoValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DtoValidator implements IDtoValidator {

    private final Validator validator;

    @Override
    public <T> T validate(T dto) {
        Set<ConstraintViolation<T>> errors = validator.validate(dto);

        if (errors.isEmpty()) {
            return dto;
        }
        else {
            String message = errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
            throw new ValueNotValidException(message);
        }
    }
}
