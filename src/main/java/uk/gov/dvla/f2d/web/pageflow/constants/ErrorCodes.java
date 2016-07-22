package uk.gov.dvla.f2d.web.pageflow.constants;

public final class ErrorCodes
{
    private ErrorCodes() {
        super();
    }

    public static final String NULL_OR_EMPTY_CODE           = "NullOrEmpty";
    public static final String NULL_OR_EMPTY_DESC           = "Mandatory field supplied was empty.";

    public static final String INVALID_OPTION_CODE          = "InvalidOption";
    public static final String INVALID_OPTION_DESC          = "Mandatory field supplied was invalid.";

    public static final String GENERAL_ERROR_CODE           = "GeneralError";
    public static final String GENERAL_ERROR_DESC           = "General error occurred in processing.";

    /**Error Code - when > maximum length.*/
    public static final String EXCEEDS_MAX_LENGTH = "ExceedsMaxLength";
    /**Error description when > maximum length.*/
    public static final String EXCEEDS_MAX_LENGTH_DESC = "Field was greater than the allowed maximum length.";

    /**Error Code - when < minimum length.*/
    public static final String LESS_THAN_MIN_LENGTH = "LessThanMinLength";
    /**Error description when < minimum length.*/
    public static final String LESS_THAN_MIN_LENGTH_DESC = "Field is less than the allowed minimum length.";

    /**Error Code - when invalid characters - based on regex check.*/
    public static final String INVALID_CHARACTERS = "InvalidChars";
    /**Error description - when invalid characters - based on regex check.*/
    public static final String INVALID_CHARACTERS_DESC = "Invalid characters in text.";

}
