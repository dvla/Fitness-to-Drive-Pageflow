package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

public class DataProcessorContinuePageImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorContinuePageImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public void apply() {
        question.setDecision(question.getOptions().trim());
    }
}
