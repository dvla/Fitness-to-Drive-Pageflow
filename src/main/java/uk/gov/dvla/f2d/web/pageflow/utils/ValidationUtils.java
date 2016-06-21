package uk.gov.dvla.f2d.web.pageflow.utils;

import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;

public final class ValidationUtils
{
    public static boolean isNullOrEmpty(final String field) {
        return (field == null || field.trim().equals(EMPTY));
    }

    private ValidationUtils() {
        super();
    }
}
