package com.rutaaprendizaje.webflux.domain.util;

import com.rutaaprendizaje.webflux.domain.exception.ValueNotValidException;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;

import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.DESCRIPTION_BLANK;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.DESCRIPTION_TOO_LONG;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.NAME_BLANK;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.NAME_TOO_LONG;

public final class TechnologyValidator {

    private TechnologyValidator() {}

    public static TechnologyModel validateTechnologyToSave(TechnologyModel technologyModel) {

        validateMinLength(technologyModel.getName(), DomainModelConstants.NAME_MIN_LENGTH, NAME_BLANK);
        validateMaxLength(technologyModel.getName(), DomainModelConstants.NAME_MAX_LENGTH, NAME_TOO_LONG);
        validateMinLength(technologyModel.getDescription(), DomainModelConstants.DESCRIPTION_MIN_LENGTH, DESCRIPTION_BLANK);
        validateMaxLength(technologyModel.getDescription(), DomainModelConstants.DESCRIPTION_MAX_LENGTH, DESCRIPTION_TOO_LONG);

        return technologyModel;
    }

    public static void validateMinLength(String text, int minLength, String message) {
        if (text == null || text.length() < minLength) {
            throw new ValueNotValidException(message);
        }
    }

    public static void validateMaxLength(String text, int maxLength, String message) {
        if (text != null && text.length() > maxLength) {
            throw new ValueNotValidException(message);
        }
    }
}
