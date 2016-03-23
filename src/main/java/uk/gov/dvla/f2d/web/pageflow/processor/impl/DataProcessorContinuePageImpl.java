package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;

import java.util.ArrayList;
import java.util.List;

public class DataProcessorContinuePageImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorContinuePageImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public List<Notification> validate() {
        question.setDecision(question.getOptions().trim());

        List<Notification> notifications = new ArrayList<>();
        return notifications;
    }
}
