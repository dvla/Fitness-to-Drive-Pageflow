package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.model.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.COMMA_SYMBOL;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.EQUALS_SYMBOL;

public class DataProcessorRadioGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorRadioGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
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
        if((answer == null) || (answer.trim().length() == 0)) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField("answer");
            notification.setCode("NullOrEmpty");
            notification.setDescription("Field supplied was empty.");
            notifications.add(notification);
        }

        // Check that the answer supplied was a valid response.
        if(!keys.contains(answer)) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField("answer");
            notification.setCode("InvalidOption");
            notification.setDescription("Field supplied was empty.");
            notifications.add(notification);
        }

        return notifications;
    }
}
