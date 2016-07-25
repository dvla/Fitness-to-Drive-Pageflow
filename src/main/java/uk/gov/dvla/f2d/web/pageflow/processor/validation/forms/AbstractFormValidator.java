package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.IFormValidator;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;
import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.isNullOrEmpty;
import static uk.gov.dvla.f2d.model.utils.StringUtils.splitAndCapitalise;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.*;

public abstract class AbstractFormValidator implements IFormValidator {
    private MedicalForm form;
    private MedicalQuestion question;
    private static final Logger logger = LoggerFactory.getLogger(AbstractFormValidator.class);

    AbstractFormValidator(MedicalForm form, MedicalQuestion question) {
        this.form = form;
        this.question = question;
    }

    public MedicalForm getForm() {
        return this.form;
    }

    MedicalQuestion getQuestion() {
        return this.question;
    }

    String getFormField(PageForm pageForm, final String name) {
        if (pageForm.getEntities().keySet().contains(name)) {
            String[] values = pageForm.getEntities().get(name);
            return (values.length > 0 ? values[0] : EMPTY);
        }
        return EMPTY;
    }

    void persistAnswersToMedicalForm(PageForm pageForm) {
        List<String> answers = new ArrayList<String>();
        for (String field : getPersistentFields()) {
            answers.add(getFormField(pageForm, field));
        }
        getQuestion().setAnswers(answers);
    }

    public List<Notification> validate(PageForm pageForm) {
        List<Notification> codes = new ArrayList<>();
        persistAnswersToMedicalForm(pageForm);

        for (String field : getPersistentFields()) {
            codes.addAll(validate(field, getFormField(pageForm, field)));
        }

        return codes;
    }

    /**
     * Validate form against DataValidation annotations.
     * @param fieldName name of the field being checked
     * @param value field value
     * @return List<Notification> - error notifications.
     */
    protected  List<Notification> validate(String fieldName, String value) {
        List<Notification> notifications = new ArrayList<>();

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    String fieldValue = (String) field.get("");
                    if (fieldValue != null && fieldValue.equals(fieldName)) {
                        if (field.isAnnotationPresent(DataValidation.class)) {
                            DataValidation dataValidation = field.getAnnotation(DataValidation.class);

                            //check null or empty
                            if (dataValidation.notNullOrEmpty()) {
                                addNotification(notifications, checkNullOrEmpty(fieldName, value));
                            }

                            //check minimum length
                            int min = dataValidation.min();
                            if (min > 0) {
                                addNotification(notifications, checkMin(fieldName, value, min));
                            }

                            //check maximum length
                            int max = field.getAnnotation(DataValidation.class).max();
                            if (max > 0) {
                                addNotification(notifications, checkMax(fieldName, value, max));
                            }

                            //check regex
                            String regex = field.getAnnotation(DataValidation.class).regex();
                            if (!isNullOrEmpty(regex) && !isNullOrEmpty(value)) {
                                addNotification(notifications, checkRegex(fieldName, value, regex));
                            }
                        }
                    }
                }
            }

        } catch (Exception e){
            logger.error("Data validation exception : " + e.getStackTrace());
        }
        return notifications;
    }

    /**
     * Check if a field is null or empty.
     * @param field name of the field being checked
     * @param value field value
     * @return notification (error notification if field is null or empty, null otherwise)
     */
    private Notification checkNullOrEmpty(String field, String value) {
        Notification notification = null;
        if (isNullOrEmpty(value)) {
            notification = new Notification();
            notification.setPage(splitAndCapitalise(getQuestion().getID(), HYPHEN));
            notification.setField(field);
            notification.setCode(NULL_OR_EMPTY_CODE);
            notification.setDescription(NULL_OR_EMPTY_DESC);
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
    private Notification checkMax(String field, String value, int max) {
        Notification notification = null;
        if (value != null && value.length() > max) {
            notification = new Notification();
            notification.setPage(splitAndCapitalise(getQuestion().getID(), HYPHEN));
            notification.setField(field);
            notification.setCode(EXCEEDS_MAX_LENGTH);
            notification.setDescription(EXCEEDS_MAX_LENGTH_DESC);
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
    private Notification checkMin(String field, String value, int min) {
        Notification notification = null;
        if (value != null && value.length() < min) {
            notification = new Notification();
            notification.setPage(splitAndCapitalise(getQuestion().getID(), HYPHEN));
            notification.setField(field);
            notification.setCode(LESS_THAN_MIN_LENGTH);
            notification.setDescription(LESS_THAN_MIN_LENGTH_DESC);
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
    private Notification checkRegex(String field, String value, String regex) {
        Notification notification = null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches()) {
            notification = new Notification();
            notification.setPage(splitAndCapitalise(getQuestion().getID(), HYPHEN));
            notification.setField(field);
            notification.setCode(INVALID_CHARACTERS);
            notification.setDescription(INVALID_CHARACTERS_DESC);
        }

        return notification;
    }

    private void addNotification(List<Notification> notifications, Notification notification) {
        if (notification != null) {
            notifications.add(notification);
        }
    }

    abstract String[] getPersistentFields();
}
