package uk.gov.dvla.f2d.web.pageflow.validation;

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
    private PageForm pageForm;

    public FormValidator(PageForm pageForm) {
        this.pageForm = pageForm;
    }

    public void validate(MedicalForm form, MedicalQuestion question) {
        form.getMessageHeader().getNotifications().clear();

        if (!question.getValidate()) {
            return;
        }

        if (question.getType().equalsIgnoreCase(Format.FORM.getName())) {
            IFormValidator validator = FormValidatorFactory.getValidator(question);
            validator.setFormData(pageForm.getEntities());

            List<Notification> notifications = validator.execute();

            if (notifications.isEmpty()) {
                question.setDecision(question.getOptions());
            } else {
                form.getMessageHeader().setNotifications(notifications);
            }

        } else {
            // All other types of data processor executed in much the same way,
            // by the page flow processor. Set the answers, and execute rules.
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
