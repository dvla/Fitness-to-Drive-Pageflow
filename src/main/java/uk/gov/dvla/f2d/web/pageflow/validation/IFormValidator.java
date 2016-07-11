package uk.gov.dvla.f2d.web.pageflow.validation;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;

import java.util.List;
import java.util.Map;

public interface IFormValidator
{
    void setFormData(Map<String, String[]> values);

    void setQuestion(MedicalQuestion question);

    List<Notification> execute();
}
