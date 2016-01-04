package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

public class DataProcessorConfirmPageImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorConfirmPageImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public void apply() {
        final String[] options = question.getOptions().trim().split("=");
        final String answer = question.getAnswers().get(0).trim();

        final String key = options[0].trim();
        final String value = options[1].trim();

        if(key.equalsIgnoreCase(answer)) {
            question.setDecision(value);
        }
    }
}
