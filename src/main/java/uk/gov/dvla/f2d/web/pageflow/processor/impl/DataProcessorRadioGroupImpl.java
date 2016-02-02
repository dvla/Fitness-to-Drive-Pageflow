package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.model.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class DataProcessorRadioGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorRadioGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    private boolean isNullOrEmpty(final String field) {
        return (field == null || field.trim().equals(EMPTY_STRING));
    }

    @Override
    public List<Notification> validate() {
        final String[] options = question.getOptions().trim().split(COMMA_SYMBOL);

        String answer = "";
        if(!(question.getAnswers().isEmpty())) {
            answer = question.getAnswers().get(0).trim();
        }

        List<String> keys = new ArrayList<>();

        for(String option : options) {
            String key = option.split(EQUALS_SYMBOL)[0].trim();
            String value = option.split(EQUALS_SYMBOL)[1].trim();

            if(key.equalsIgnoreCase(answer)) {
                question.setDecision(value);
            }

            keys.add(key);
        }

        List<Notification> notifications = new ArrayList<>();

        // Check that an answer was supplied for this question.
        if(isNullOrEmpty(answer)) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField(ANSWER_FIELD);
            notification.setCode("NullOrEmpty");
            notification.setDescription("Field supplied was empty.");
            notifications.add(notification);
        }

        // Check that the answer supplied was a valid response.
        if(!isNullOrEmpty(answer) && !keys.contains(answer)) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField(ANSWER_FIELD);
            notification.setCode("InvalidOption");
            notification.setDescription("Field supplied was empty.");
            notifications.add(notification);
        }

        return notifications;
    }
}
