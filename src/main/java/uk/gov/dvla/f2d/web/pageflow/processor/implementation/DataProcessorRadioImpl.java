package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.helpers.FormHelper;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;

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
        logger.debug("Constructor entered.");
        this.question = question;
        loadConfiguration();
    }

    private void loadConfiguration() {
        logger.debug("begin: loadConfiguration() method");
        try {
            String config = question.getConfiguration();

            TypeReference<Map<String, String>> type = new TypeReference<Map<String, String>>() {};
            Map<String, String> values = new ObjectMapper().readValue(config, type);

            logger.debug("Map: "+values.toString());

            for(String key : values.keySet()) {
                String value = values.get(key);
                logger.debug("Key: %s, Value: %s", key, value);
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Notification> validate() {
        logger.debug("begin: validate() method");

        final String[] options = question.getOptions().trim().split(COMMA);

        String answer = EMPTY;
        if(!(question.getAnswers().isEmpty())) {
            answer = question.getAnswers().get(0).trim();
        }

        List<String> keys = new ArrayList<>();

        for(String option : options) {
            String key = option.split(EQUALS)[0].trim();
            String value = option.split(EQUALS)[1].trim();

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
            notification.setCode(NULL_OR_EMPTY_CODE);
            notification.setDescription(NULL_OR_EMPTY_DESC);
            notifications.add(notification);
        }

        // Check that the answer supplied was a valid response.
        if(!isNullOrEmpty(answer) && !keys.contains(answer)) {
            Notification notification = new Notification();
            notification.setPage(FormHelper.capitalise(question));
            notification.setField(ANSWER_FIELD);
            notification.setCode(INVALID_OPTION_CODE);
            notification.setDescription(INVALID_OPTION_DESC);
            notifications.add(notification);
        }

        logger.debug("finish: validate() method");

        return notifications;
    }
}
