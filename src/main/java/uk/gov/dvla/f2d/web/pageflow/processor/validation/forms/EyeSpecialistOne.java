package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.IFormValidator;

public class EyeSpecialistOne extends MedicalContactDetails implements IFormValidator
{
    public EyeSpecialistOne(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }
}
