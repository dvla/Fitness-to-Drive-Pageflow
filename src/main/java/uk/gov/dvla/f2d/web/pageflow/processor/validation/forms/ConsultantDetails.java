package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.IFormValidator;

public class ConsultantDetails extends MedicalContactDetails implements IFormValidator
{
    public ConsultantDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }
}

