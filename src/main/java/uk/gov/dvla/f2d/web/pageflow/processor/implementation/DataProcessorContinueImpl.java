package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.exceptions.SystemException;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.ContinueComponentConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HASH;

public class DataProcessorContinueImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorContinueImpl.class);

    private MedicalQuestion question;

    public DataProcessorContinueImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public Map<String, String> getConfiguration() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ContinueComponentConfiguration configuration = mapper.readValue(
                    question.getConfiguration(), ContinueComponentConfiguration.class
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

        logger.debug("finish: validate() method");

        return notifications;
    }
}
