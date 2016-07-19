package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

public class OphthalmologistDetails extends AbstractFormValidator
{
    private static final String CONSULTANT_NAME     = "consultantName";
    private static final String HOSPITAL_NAME       = "hospitalName";
    private static final String POST_TOWN           = "postTown";
    private static final String POST_CODE           = "postCode";
    private static final String PHONE_NUMBER        = "phoneNumber";

    public OphthalmologistDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    String[] getPersistentFields() {
        return new String[] { CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN, POST_CODE, PHONE_NUMBER};
    }

    String[] getMandatoryFields() {
        return new String[] { CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN};
    }
}
