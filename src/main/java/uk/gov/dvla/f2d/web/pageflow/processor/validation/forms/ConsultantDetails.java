package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.model.utils.StringUtils;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.IFormValidator;
import uk.gov.dvla.f2d.web.pageflow.utils.PageFlowUtils;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.AT_LEAST_ONE_FIELD;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_CODE;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_DESC;

public class ConsultantDetails extends AbstractFormValidator implements IFormValidator
{
    static final String CONSULTANT_NAME     = "consultantName";
    static final String CLINIC_NAME         = "clinicName";
    static final String HOSPITAL_NAME       = "hospitalName";
    static final String POST_TOWN           = "postTown";
    static final String POST_CODE           = "postCode";
    static final String PHONE_NUMBER        = "phoneNumber";

    private static final String[] PERSISTED_FIELDS = {
            CONSULTANT_NAME, CLINIC_NAME, HOSPITAL_NAME, POST_TOWN, POST_CODE, PHONE_NUMBER
    };

    private static final String[] MANDATORY_FIELDS = {
            CONSULTANT_NAME, CLINIC_NAME, HOSPITAL_NAME, POST_TOWN
    };

    public ConsultantDetails(MedicalForm form, MedicalQuestion question) {
        super(form, question);
    }

    @Override
    public String[] getPersistentFields() {
        return PERSISTED_FIELDS;
    }

    @Override
    public String[] getMandatoryFields() {
        return MANDATORY_FIELDS;
    }

    @Override
    List<Notification> checkMandatoryFieldsSupplied(PageForm pageForm) {
        return execute(pageForm);
    }

    public List<Notification> execute(PageForm pageForm) {

        List<Notification> codes = new ArrayList<>();
        if(StringUtils.isNullOrEmpty(getFormField(pageForm, POST_TOWN))) {
            codes.add(createNotification(POST_TOWN, NULL_OR_EMPTY_CODE, NULL_OR_EMPTY_DESC));
        }
        if(StringUtils.isNullOrEmpty(getFormField(pageForm, CONSULTANT_NAME))) {
            codes.add(createNotification(CONSULTANT_NAME, NULL_OR_EMPTY_CODE, NULL_OR_EMPTY_DESC));
        }
        if(StringUtils.isNullOrEmpty(getFormField(pageForm, CLINIC_NAME)) &&
                StringUtils.isNullOrEmpty(getFormField(pageForm, HOSPITAL_NAME)) ) {
            codes.add(createNotification(CLINIC_NAME, AT_LEAST_ONE_FIELD, ""));
            codes.add(createNotification(HOSPITAL_NAME, AT_LEAST_ONE_FIELD, ""));
        }

        return codes;
    }

    private Notification createNotification(String field, String code, String description) {
        Notification notification = new Notification();
        notification.setPage(PageFlowUtils.capitalise(getQuestion()));
        notification.setField(field);
        notification.setCode(code);
        notification.setDescription(description);
        return notification;
    }
}

