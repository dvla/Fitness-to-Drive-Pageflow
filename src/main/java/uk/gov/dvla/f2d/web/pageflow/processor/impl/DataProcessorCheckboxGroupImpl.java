package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;
import uk.gov.dvla.f2d.web.pageflow.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.ANSWER_FIELD;

public class DataProcessorCheckboxGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorCheckboxGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public List<Notification> validate() {
        LogUtils.debug(this.getClass(), "Answers: "+question.getAnswers());
        LogUtils.debug(this.getClass(), "  - Size: "+question.getAnswers().size());
        LogUtils.debug(this.getClass(), "  - Empty: "+question.getAnswers().isEmpty());
        LogUtils.debug(this.getClass(), "Options: ["+question.getOptions()+"]");

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
