package uk.gov.dvla.f2d.web.pageflow.exceptions;

public class PageValidationException extends Exception
{
    private String code;

    public PageValidationException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
