package com.rutaaprendizaje.webflux.domain.util;

public final class DomainExceptionConstants {

    private DomainExceptionConstants() {}

    public static final String TECHNOLOGY_NOT_FOUND = "Technology not found";
    public static final String AT_LEAST_ONE_TECHNOLOGY_NOT_FOUND = "At least one technology not found";
    public static final String TECHNOLOGY_ALREADY_EXISTS = "A different technology already exists with this name";

    public static final String NAME_BLANK = "Name cannot be blank";
    public static final String NAME_TOO_LONG = "Name must have less than 51 characters";
    public static final String DESCRIPTION_BLANK = "Description cannot be blank";
    public static final String DESCRIPTION_TOO_LONG = "Description must have less than 91 characters";

}
