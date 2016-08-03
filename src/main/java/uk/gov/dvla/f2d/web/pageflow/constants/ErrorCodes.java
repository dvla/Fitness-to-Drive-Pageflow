package uk.gov.dvla.f2d.web.pageflow.constants;

public final class ErrorCodes
{
    private ErrorCodes() {
        super();
    }

    public static final String NULL_OR_EMPTY_CODE           = "NullOrEmpty";
    public static final String NULL_OR_EMPTY_DESC           = "Mandatory field supplied was empty.";
    public static final String AT_LEAST_ONE_FIELD           = "AtLeastOneField";
    public static final String AT_LEAST_ONE_FIELD_DESC      = "At least one field needed.";

    public static final String INVALID_OPTION_CODE          = "InvalidOption";
    public static final String INVALID_OPTION_DESC          = "Mandatory field supplied was invalid.";

    public static final String GENERAL_ERROR_CODE           = "GeneralError";
    public static final String GENERAL_ERROR_DESC           = "General error occurred in processing.";

    public static final String MAX_LENGTH = "MaxLength";
    public static final String MAX_LENGTH_DESC = "Field was greater than the allowed maximum length.";

    public static final String MIN_LENGTH = "MinLength";
    public static final String MIN_LENGTH_DESC = "Field is less than the allowed minimum length.";

    public static final String INVALID_CHARACTERS = "InvalidChars";
    public static final String INVALID_CHARACTERS_DESC = "Invalid characters in text.";
}
