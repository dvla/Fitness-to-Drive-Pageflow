package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.exceptions.SystemException;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.model.utils.StringUtils;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.CheckBoxComponentConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HASH;
import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.ANSWER_FIELD;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_CODE;
import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.NULL_OR_EMPTY_DESC;

public class DataProcessorCheckBoxImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorCheckBoxImpl.class);

    private MedicalQuestion question;

    public DataProcessorCheckBoxImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public Map<String, String> getConfiguration() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CheckBoxComponentConfiguration configuration = mapper.readValue(
                    question.getConfiguration(), CheckBoxComponentConfiguration.class
            );
            return configuration.getOptions();

        } catch(IOException ex) {
            throw new SystemException(ex);
        }
    }

    @Override
    public List<Notification> validate() {
        logger.debug("begin: validate() method");

        List<Notification> notifications = new ArrayList<>();

        Map<String, String> configuration = getConfiguration();
        question.setDecision(configuration.get(HASH));

        if(question.getAnswers().isEmpty()) {
            Notification notification = new Notification();
            notification.setPage(StringUtils.splitAndCapitalise(question.getID(), HYPHEN));
            notification.setField(ANSWER_FIELD);
            notification.setCode(NULL_OR_EMPTY_CODE);
            notification.setDescription(NULL_OR_EMPTY_DESC);
            notifications.add(notification);
        }

        logger.debug("finish: validate() method");

        return notifications;
    }
}
