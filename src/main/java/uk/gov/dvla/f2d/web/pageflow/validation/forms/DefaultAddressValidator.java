package uk.gov.dvla.f2d.web.pageflow.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.validation.IFormValidator;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_CODE;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_DESC;

public class DefaultAddressValidator extends AbstractFormValidator implements IFormValidator
{
    private static final String ADDRESS_LINE_1  = "addressLine1";
    private static final String ADDRESS_LINE_2  = "addressLine2";
    private static final String ADDRESS_LINE_3  = "addressLine3";
    private static final String POST_TOWN       = "postTown";
    private static final String POST_CODE       = "postCode";

    private static final String[] PERSISTED_FIELDS = {
            ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, POST_TOWN, POST_CODE
    };

    private static final String[] MANDATORY_FIELDS = {
            ADDRESS_LINE_1, POST_TOWN, POST_CODE
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
