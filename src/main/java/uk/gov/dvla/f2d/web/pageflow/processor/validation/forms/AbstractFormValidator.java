package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.IFormValidator;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;
import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.isNullOrEmpty;
import static uk.gov.dvla.f2d.model.utils.StringUtils.splitAndCapitalise;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_CODE;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_DESC;

public abstract class AbstractFormValidator implements IFormValidator
{
    private MedicalForm form;
    private MedicalQuestion question;

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

    List<Notification> checkMandatoryFieldsSupplied(PageForm pageForm) {
        List<Notification> codes = new ArrayList<>();
        for (String field : getMandatoryFields()) {
            if (isNullOrEmpty(getFormField(pageForm, field))) {
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

    public List<Notification> validate(PageForm pageForm) {
        persistAnswersToMedicalForm(pageForm);

        return checkMandatoryFieldsSupplied(pageForm);
    }

    abstract String[] getPersistentFields();
    abstract String[] getMandatoryFields();
}
