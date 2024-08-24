package com.rutaaprendizaje.webflux.domain.util;

import com.rutaaprendizaje.webflux.domain.exception.ValueNotValidException;
import com.rutaaprendizaje.webflux.domain.model.TechnologyModel;
import org.junit.jupiter.api.Test;

import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.DESCRIPTION_BLANK;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.DESCRIPTION_TOO_LONG;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.NAME_BLANK;
import static com.rutaaprendizaje.webflux.domain.util.DomainExceptionConstants.NAME_TOO_LONG;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TechnologyValidatorTest {

    @Test
    void validateTechnologyToSave_ShouldNotThrowException_WhenTechnologyModelIsValid() {
        String validName = "Valid Name";
        String validDescription = "Valid description";

        TechnologyModel model = new TechnologyModel(null, validName, validDescription);

        TechnologyModel result = assertDoesNotThrow(() -> TechnologyValidator.validateTechnologyToSave(model));

        assertEquals(model, result);
    }

    @Test
    void validateTechnologyToSave_ShouldThrowException_WhenNameIsBlank() {
        TechnologyModel model = new TechnologyModel(null, "", "Valid description");

        assertThrows(ValueNotValidException.class, () -> TechnologyValidator.validateTechnologyToSave(model),
                NAME_BLANK);
    }

    @Test
    void validateTechnologyToSave_ShouldThrowException_WhenNameIsTooLong() {
        String longName = "a".repeat(DomainModelConstants.NAME_MAX_LENGTH + 1);
        TechnologyModel model = new TechnologyModel(null, longName, "Valid description");

        assertThrows(ValueNotValidException.class, () -> TechnologyValidator.validateTechnologyToSave(model),
                NAME_TOO_LONG);
    }

    @Test
    void validateTechnologyToSave_ShouldThrowException_WhenDescriptionIsBlank() {
        TechnologyModel model = new TechnologyModel(null, "Valid name", "");

        assertThrows(ValueNotValidException.class, () -> TechnologyValidator.validateTechnologyToSave(model),
                DESCRIPTION_BLANK);
    }

    @Test
    void validateTechnologyToSave_ShouldThrowException_WhenDescriptionIsTooLong() {
        String longDescription = "a".repeat(DomainModelConstants.DESCRIPTION_MAX_LENGTH + 1);
        TechnologyModel model = new TechnologyModel(null, "Valid name", longDescription);

        assertThrows(ValueNotValidException.class, () -> TechnologyValidator.validateTechnologyToSave(model),
                DESCRIPTION_TOO_LONG);
    }
}