package uk.gov.dvla.f2d.web.pageflow.processor.validation;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.DataProcessorFactory;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;

import java.util.Arrays;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.ANSWER_FIELD;

public class FormValidator
{
    private MedicalForm form;
    private MedicalQuestion question;

    public FormValidator(MedicalForm form, MedicalQuestion question) {
        this.form = form;
        this.question = question;
    }

    public void validate(PageForm pageForm) {
        form.getMessageHeader().getNotifications().clear();

        if (!(question.getValidate())) {
            return;
        }

        if (question.getType().equalsIgnoreCase(Format.FORM.getName())) {
            FormValidatorFactory factory = new FormValidatorFactory(form, question);
            IFormValidator validator = factory.getValidator();

            List<Notification> notifications = validator.validate(pageForm);

            if (notifications.isEmpty()) {
                question.setDecision(question.getOptions());
            } else {
                form.getMessageHeader().setNotifications(notifications);
            }

        } else {
            // All other types of data processor executed in much the same way,
            // by the page flow processor. Set the answers, and validate rules.
            String[] answers = pageForm.getEntities().get(ANSWER_FIELD);

            // Safety check that all user data provided is ready for processing.
            if (answers == null) {
                answers = new String[0];
            }

            question.setAnswers(Arrays.asList(answers));

            DataProcessorFactory factory = new DataProcessorFactory();
            IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

            List<Notification> notifications = processor.validate();
            if (!(notifications.isEmpty())) {
                form.getMessageHeader().setNotifications(notifications);
            }
        }
    }
}
