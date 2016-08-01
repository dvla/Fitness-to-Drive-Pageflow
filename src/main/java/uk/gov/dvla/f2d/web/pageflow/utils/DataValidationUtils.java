package uk.gov.dvla.f2d.web.pageflow.utils;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.isNullOrEmpty;
import static uk.gov.dvla.f2d.model.utils.StringUtils.splitAndCapitalise;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.*;

public class DataValidationUtils {

    public static List<Notification> validateData(DataValidation dataValidation,
        MedicalQuestion question, String field, String value) {
        List<Notification> notifications = new ArrayList<>();

        if (dataValidation.notNullOrEmpty()) {
            addNotification(notifications, checkNullOrEmpty(question, field, value));
        }

        //check minimum length
        int min = dataValidation.min();
        if (min > 0) {
            addNotification(notifications, checkMin(question, field, value, min));
        }

        //check maximum length
        int max = dataValidation.max();
        if (max > 0) {
            addNotification(notifications, checkMax(question, field, value, max));
        }

        //check regex
        String regex = dataValidation.regex();
        if (!isNullOrEmpty(regex) && !isNullOrEmpty(value)) {
            addNotification(notifications, checkRegex(question, field, value, regex));
        }

    return notifications;
    }
    /**
     * Check if a field is null or empty.
     * @param field name of the field being checked
     * @param value field value
     * @return notification (error notification if field is null or empty, null otherwise)
     */
    public static Notification checkNullOrEmpty(MedicalQuestion question, String field, String value) {
        Notification notification = null;
        if (isNullOrEmpty(value)) {
            notification = createNotification(question, field, NULL_OR_EMPTY_CODE, NULL_OR_EMPTY_DESC);
        }

        return notification;
    }

    /**
     * Check maximum length for a field.
     * @param field name of the field being checked
     * @param value field value
     * @param max allowed max characters
     * @return notification (null if <= max length, error notification if > max length)
     */
    public static Notification checkMax(MedicalQuestion question, String field, String value, int max) {
        Notification notification = null;
        if (value != null && value.length() > max) {
            notification = createNotification(question, field, MAX_LENGTH, MAX_LENGTH_DESC);
        }

        return notification;
    }

    /**
     * Check minimum length for a field.
     * @param field name of the field being checked
     * @param value field value
     * @param min allowed min characters
     * @return notification (null if >= min length, error notification if < min length)
     **/
    public static Notification checkMin(MedicalQuestion question, String field, String value, int min) {
        Notification notification = null;
        if (value != null && value.length() < min) {
            notification = createNotification(question, field, MIN_LENGTH, MIN_LENGTH_DESC);
        }

        return notification;
    }

    /**
     * Chck regular expression for valid data.
     * @param field name of the field being checked
     * @param value field value
     * @param regex regular expression for data allowed for the field being passed
     * @return notification (null if regular expression matches else error notification)
     */
    public static Notification checkRegex(MedicalQuestion question, String field, String value, String regex) {
        Notification notification = null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches()) {
            notification = createNotification(question, field, INVALID_CHARACTERS, INVALID_CHARACTERS_DESC);
        }

        return notification;
    }

    private static Notification createNotification(MedicalQuestion question, String field, String code, String description) {
        Notification notification = new Notification();
        notification.setPage(splitAndCapitalise(question.getID(), HYPHEN));
        notification.setField(field);
        notification.setCode(code);
        notification.setDescription(description);
        return notification;
    }

    private static void addNotification(List<Notification> notifications, Notification notification) {
        if (notification != null) {
            notifications.add(notification);
        }
    }
}
