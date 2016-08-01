package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import org.apache.commons.lang3.exception.ExceptionUtils;
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

import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;
import static uk.gov.dvla.f2d.web.pageflow.utils.DataValidationUtils.validateData;

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
        List<String> answers = new ArrayList<>();
        for (String field : getPersistentFields()) {
            answers.add(getFormField(pageForm, field));
        }
        getQuestion().setAnswers(answers);
    }

    public List<Notification> validate(PageForm pageForm) {
        List<Notification> codes = new ArrayList<>();
        persistAnswersToMedicalForm(pageForm);

        for (String field : getPersistentFields()) {
            codes.addAll(buildNotifications(field, getFormField(pageForm, field)));
        }

        return codes;
    }

    /**
     * Validate form against DataValidation annotations.
     * @param fieldName name of the field being checked
     * @param value field value
     * @return List<Notification> - error notifications.
     */
    private List<Notification> buildNotifications(String fieldName, String value) {
        List<Notification> notifications = new ArrayList<>();

        try {
            DataValidation dataValidation = getDataValidation(fieldName, value);
            if (dataValidation != null) {
                notifications = validateData(dataValidation, getQuestion(), fieldName, value);
            }
        } catch (Exception e){
            logger.error("Data validation exception : " + ExceptionUtils.getStackTrace(e));
        }
        return notifications;
    }

    /**
     * Get DataValidation annotaition for a persisted field.
     * @param fieldName persisted field name
     * @param value value e.g. phoneNumber.
     * @return DataValidation annotation, if exists.
     * @throws IllegalAccessException
     */
    private DataValidation getDataValidation(String fieldName, String value) throws IllegalAccessException{
        DataValidation dataValidation = null;

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                String fieldValue = (String) field.get(EMPTY);
                if (fieldValue != null && fieldValue.equals(fieldName)) {
                    if (field.isAnnotationPresent(DataValidation.class)) {
                        dataValidation = field.getAnnotation(DataValidation.class);
                    }
                break;
                }
            }
        }

        return dataValidation;
    }

    abstract String[] getPersistentFields();
}
