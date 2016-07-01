package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;

import java.util.ArrayList;
import java.util.List;

public class DataProcessorFormImpl implements IDataQuestionProcessor
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorFormImpl.class);

    private MedicalQuestion question;

    public DataProcessorFormImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public List<Notification> validate() {
        logger.debug("begin: validate() method");
        question.setDecision(question.getOptions().trim());

        List<Notification> notifications = new ArrayList<>();

        logger.debug("finish: validate() method");

        return notifications;
    }
}
