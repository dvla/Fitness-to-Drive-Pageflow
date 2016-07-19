package uk.gov.dvla.f2d.web.pageflow.processor.validation;

import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;

import java.util.List;

public interface IFormValidator
{
    List<Notification> validate(PageForm form);
}
