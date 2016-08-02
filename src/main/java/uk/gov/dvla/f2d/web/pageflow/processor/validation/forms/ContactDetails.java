package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;

public final class ContactDetails extends AbstractFormValidator {
    @DataValidation(max = 12, regex = "[0-9\\s]*")
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL_ADDRESS = "emailAddress";

    public ContactDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    String[] getPersistentFields() {
        return new String[] { PHONE_NUMBER, EMAIL_ADDRESS };
    }
}
