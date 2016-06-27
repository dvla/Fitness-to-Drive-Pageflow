package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.components.RadioComponent;

import java.io.IOException;
import java.util.*;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.ANSWER_FIELD;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.*;
import static uk.gov.dvla.f2d.model.utils.StringUtils.*;

public class DataProcessorRadioImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorRadioImpl.class);

    private MedicalQuestion question;

    public DataProcessorRadioImpl(MedicalQuestion question) {
        this.question = question;
    }

    private RadioComponent getComponent() throws IOException {
        return new ObjectMapper().readValue(question.getConfiguration(), RadioComponent.class);
    }

    @Override
    public List<Notification> validate() {
        logger.debug("begin: validate() method");

        List<Notification> notifications = new ArrayList<>();
        try {
            // Find the user the user provided.
            String answer = EMPTY;
            if(!(question.getAnswers().isEmpty())) {
                answer = question.getAnswers().get(0).trim();
            }

            // Check that an answer was supplied for this question.
            if (isNullOrEmpty(answer)) {
                Notification notification = new Notification();
                notification.setPage(FormHelper.capitalise(question));
                notification.setField(ANSWER_FIELD);
                notification.setCode(NULL_OR_EMPTY_CODE);
                notification.setDescription(NULL_OR_EMPTY_DESC);
                notifications.add(notification);
            }

            RadioComponent component = getComponent();

            Set<String> keys = component.getOptions().keySet();
            for(String key : keys) {
                if(key.equals(answer)) {
                    question.setDecision(component.getOptions().get(key));
                }
            }

            // Check that the answer supplied was a valid response.
            if (!isNullOrEmpty(answer) && !keys.contains(answer)) {
                Notification notification = new Notification();
                notification.setPage(FormHelper.capitalise(question));
                notification.setField(ANSWER_FIELD);
                notification.setCode(INVALID_OPTION_CODE);
                notification.setDescription(INVALID_OPTION_DESC);
                notifications.add(notification);
            }

        } catch(IOException ex) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField(ANSWER_FIELD);
            notification.setCode(GENERAL_ERROR_CODE);
            notification.setDescription(GENERAL_ERROR_DESC);
            notifications.add(notification);
        }

        logger.debug("finish: validate() method");

        return notifications;
    }
}
