package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.ControllerComponentConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProcessorControllerImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorRadioImpl.class);

    private MedicalQuestion question;

    public DataProcessorControllerImpl(MedicalQuestion question) {
        this.question = question;
    }

    private ControllerComponentConfiguration getComponent() throws IOException {
        return new ObjectMapper().readValue(question.getConfiguration(), ControllerComponentConfiguration.class);
    }

    @Override
    public List<Notification> validate() {
        logger.debug("begin: validate() method");

        List<Notification> notifications = new ArrayList<>();

        logger.debug("finish: validate() method");

        return notifications;
    }
}
