package uk.gov.dvla.f2d.web.pageflow.exceptions;

import uk.gov.dvla.f2d.model.exceptions.ApplicationException;

public class PageNotFoundException extends ApplicationException
{
    public PageNotFoundException(String message) {
        super(message);
    }
}
