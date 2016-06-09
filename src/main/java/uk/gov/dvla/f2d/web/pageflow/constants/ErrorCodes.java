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

}
