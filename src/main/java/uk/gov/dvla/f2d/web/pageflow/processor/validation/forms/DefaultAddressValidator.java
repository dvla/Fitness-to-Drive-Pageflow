package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;

public class DefaultAddressValidator extends AbstractFormValidator {
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String ADDRESS_LINE_1  = "addressLine1";
    private static final String ADDRESS_LINE_2  = "addressLine2";
    private static final String ADDRESS_LINE_3  = "addressLine3";
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String POST_TOWN       = "postTown";
    @DataValidation(max = 100, notNullOrEmpty = true)
    private static final String POST_CODE       = "postCode";

    public DefaultAddressValidator(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    String[] getPersistentFields() {
        return new String[] { ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, POST_TOWN, POST_CODE};
    }
}
