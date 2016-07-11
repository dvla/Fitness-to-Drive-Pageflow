package uk.gov.dvla.f2d.web.pageflow.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.validation.IFormValidator;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_CODE;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_DESC;

public class OphthalmologistDetails extends AbstractFormValidator implements IFormValidator
{
    private static final String CONSULTANT_NAME     = "consultantName";
    private static final String HOSPITAL_NAME       = "hospitalName";
    private static final String POST_TOWN           = "postTown";
    private static final String POST_CODE           = "postCode";
    private static final String PHONE_NUMBER        = "phoneNumber";

    private static final String[] PERSISTED_FIELDS = {
        CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN, POST_CODE, PHONE_NUMBER
    };

    private static final String[] MANDATORY_FIELDS = {
        CONSULTANT_NAME, HOSPITAL_NAME, POST_TOWN
    };

    public List<Notification> execute() {
        persistFields(PERSISTED_FIELDS);

        List<Notification> codes = new ArrayList<Notification>();
        for (String field : MANDATORY_FIELDS) {
            if (isNullOrEmpty(getFormField(field))) {
                Notification notification = new Notification();
                notification.setPage(splitAndCapitalise(getQuestion().getID(), HYPHEN));
                notification.setField(field);
                notification.setCode(NULL_OR_EMPTY_CODE);
                notification.setDescription(NULL_OR_EMPTY_DESC);
                codes.add(notification);
            }
        }

        return codes;
    }
}
