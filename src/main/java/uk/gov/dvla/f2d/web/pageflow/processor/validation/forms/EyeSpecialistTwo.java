package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;

public class EyeSpecialistTwo extends AbstractFormValidator {
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String CONSULTANT_NAME     = "consultantName";
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String HOSPITAL_NAME       = "hospitalName";
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String POST_TOWN           = "postTown";
    private static final String POST_CODE           = "postCode";
    private static final String PHONE_NUMBER        = "phoneNumber";

    public EyeSpecialistTwo(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    String[] getPersistentFields() {
        return new String[] { CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN, POST_CODE, PHONE_NUMBER};
    }

    String[] getMandatoryFields() {
        return new String[] { CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN};
    }
}
