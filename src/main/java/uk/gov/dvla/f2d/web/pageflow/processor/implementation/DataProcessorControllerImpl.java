package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.exceptions.SystemException;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.ControllerComponentConfiguration;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.RadioComponentConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataProcessorControllerImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorRadioImpl.class);

    private MedicalQuestion question;

    public DataProcessorControllerImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public Map<String, String> getConfiguration() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ControllerComponentConfiguration configuration = mapper.readValue(
                    question.getConfiguration(), ControllerComponentConfiguration.class
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

        logger.debug("finish: validate() method");

        return notifications;
    }
}
