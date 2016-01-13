package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.exceptions.PageValidationException;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

public class DataProcessorCheckboxGroupImpl implements IDataQuestionProcessor
{
    private MedicalQuestion question;

    DataProcessorCheckboxGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public void apply() throws PageValidationException {
        question.setDecision(question.getOptions().trim());
    }

    /*
    @Override
    public void apply() {
        final String[] options = question.getOptions().split(",");
        final Integer answer = findAnswer();

        for (String option : options) {
            String key = option.split("=")[0].trim();
            String value = option.split("=")[1].trim();

            if (key.equalsIgnoreCase(answer.toString())) {
                question.setDecision(value);
            }
        }
    }

    private Integer findAnswer() {
        return question.getAnswers().stream().mapToInt(s -> Integer.parseInt(s)).sum();
    }
    */
}
