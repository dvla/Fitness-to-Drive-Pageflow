package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.ANSWER_FIELD;

public class DataProcessorCheckboxGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    private Logger logger = LoggerFactory.getLogger(DataProcessorCheckboxGroupImpl.class);

    DataProcessorCheckboxGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public List<Notification> validate() {
        logger.debug("Answers: "+question.getAnswers());
        logger.debug("Size: "+question.getAnswers().size());
        logger.debug("Empty?: "+question.getAnswers().isEmpty());
        logger.debug("Options: ["+question.getOptions()+"]");

        question.setDecision(question.getOptions().trim());

        List<Notification> notifications = new ArrayList<>();

        if(question.getAnswers().isEmpty()) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField(ANSWER_FIELD);
            notification.setCode("NullOrEmpty");
            notification.setDescription("Field supplied was empty.");
            notifications.add(notification);
        }

        return notifications;
    }

    /*
    @Override
    public void validate() {
        final String[] options = question.getOptions().split(",");
        final Integer answer = findAnswer();

        for (String option : options) {
            String key = option.split("=")[0].trim();
            String value = option.split("=")[1].trim();

            if (key.equalsIgnoreCase(answer.toString())) {
                question.setDecision(value);
            }
        }
    }

    private Integer findAnswer() {
        return question.getAnswers().stream().mapToInt(s -> Integer.parseInt(s)).sum();
    }
    */
}
