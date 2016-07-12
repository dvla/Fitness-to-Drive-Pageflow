package uk.gov.dvla.f2d.web.pageflow.exceptions;

import uk.gov.dvla.f2d.model.exceptions.ApplicationException;

public class VerificationRequiredException extends ApplicationException
{
    public VerificationRequiredException(final String message) {
        super(message);
    }
}
