package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;

import java.util.ArrayList;
import java.util.List;

public class DataProcessorContinueImpl implements IDataQuestionProcessor
{
    //private static final Logger logger = LoggerFactory.getLogger(DataProcessorContinueImpl.class);

    private MedicalQuestion question;

    public DataProcessorContinueImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public List<Notification> validate() {
        question.setDecision(question.getOptions().trim());

        List<Notification> notifications = new ArrayList<>();
        return notifications;
    }
}
