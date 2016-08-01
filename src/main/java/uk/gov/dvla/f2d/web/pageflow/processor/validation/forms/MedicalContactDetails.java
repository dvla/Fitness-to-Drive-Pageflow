package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;
import uk.gov.dvla.f2d.web.pageflow.utils.DataValidationUtils;

import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.AT_LEAST_ONE_FIELD;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.AT_LEAST_ONE_FIELD_DESC;

/**
 * Base class for medical contact details
 */
public class MedicalContactDetails extends AbstractFormValidator {
    @DataValidation(max = 100, notNullOrEmpty = true)
    public static final String CONSULTANT_NAME = "consultantName";
    public static final String CLINIC_NAME = "clinicName";
    public static final String HOSPITAL_NAME = "hospitalName";
    @DataValidation(max = 100, notNullOrEmpty = true)
    public static final String POST_TOWN = "postTown";
    public static final String POST_CODE = "postCode";
    public static final String PHONE_NUMBER = "phoneNumber";


    private static final String[] PERSISTED_FIELDS = {
            //order is important because of mapping answers to a comma separated string
            CONSULTANT_NAME, HOSPITAL_NAME, CLINIC_NAME, POST_TOWN, POST_CODE, PHONE_NUMBER
    };

    public MedicalContactDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    @Override
    public String[] getPersistentFields() {
        return PERSISTED_FIELDS;
    }

    @Override
    public List<Notification> validate(PageForm pageForm) {
        List<Notification> codes = super.validate(pageForm);

        if (DataValidationUtils.checkNullOrEmpty(getQuestion(), CLINIC_NAME, getFormField(pageForm, CLINIC_NAME)) != null &&
                DataValidationUtils.checkNullOrEmpty(getQuestion(), HOSPITAL_NAME, getFormField(pageForm, HOSPITAL_NAME)) != null) {
            codes.add(DataValidationUtils.createNotification(getQuestion(), CLINIC_NAME, AT_LEAST_ONE_FIELD, AT_LEAST_ONE_FIELD_DESC));
            codes.add(DataValidationUtils.createNotification(getQuestion(), HOSPITAL_NAME, AT_LEAST_ONE_FIELD, AT_LEAST_ONE_FIELD_DESC));
        }

        return codes;
    }
}

