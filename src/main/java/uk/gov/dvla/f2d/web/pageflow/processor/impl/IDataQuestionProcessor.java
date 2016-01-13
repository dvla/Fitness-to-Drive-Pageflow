package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.exceptions.PageValidationException;

public interface IDataQuestionProcessor
{
    void apply() throws PageValidationException;
}
