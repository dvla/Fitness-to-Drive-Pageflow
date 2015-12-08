package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

public class DataProcessorRadioGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorRadioGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public void apply() {
        final String[] options = question.getOptions().split(",");
        final String answer = question.getAnswers().get(0);

        for(String option : options) {
            String key = option.split("=")[0];
            String value = option.split("=")[1];

            if(key.equalsIgnoreCase(answer)) {
                question.setDecision(value);
            }
        }
    }
}
