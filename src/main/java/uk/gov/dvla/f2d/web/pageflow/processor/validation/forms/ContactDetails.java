package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

public final class ContactDetails extends AbstractFormValidator
{
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL_ADDRESS = "emailAddress";

    public ContactDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    String[] getPersistentFields() {
        return new String[] { PHONE_NUMBER, EMAIL_ADDRESS };
    }

    String[] getMandatoryFields() {
        return new String[] { };
    }
}
