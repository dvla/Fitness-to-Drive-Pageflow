package uk.gov.dvla.f2d.web.pageflow.exceptions;

import uk.gov.dvla.f2d.model.exceptions.ApplicationException;

public class QuestionNotFoundException extends ApplicationException
{
    public QuestionNotFoundException(final String message) {
        super(message);
    }
}
