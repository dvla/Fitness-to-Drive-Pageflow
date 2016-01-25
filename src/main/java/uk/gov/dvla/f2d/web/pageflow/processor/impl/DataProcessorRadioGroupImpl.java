package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.exceptions.PageValidationException;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

import java.util.ArrayList;
import java.util.List;

public class DataProcessorRadioGroupImpl implements IDataQuestionProcessor
{
    private static final String COMMA_SYMBOL        = ",";
    private static final String EQUALS_SYMBOL       = "=";

    private MedicalQuestion question;

    DataProcessorRadioGroupImpl(MedicalQuestion newQuestion) {
        this.question = newQuestion;
    }

    @Override
    public void apply() throws PageValidationException {
        final String[] options = question.getOptions().trim().split(COMMA_SYMBOL);

        String answer = "";
        if(!(question.getAnswers().isEmpty())) {
            answer = question.getAnswers().get(0).trim();
        }

        List<String> keys = new ArrayList<>();

        for(String option : options) {
            String key = option.split(EQUALS_SYMBOL)[0].trim();
            String value = option.split(EQUALS_SYMBOL)[1].trim();

            if(key.equalsIgnoreCase(answer)) {
                question.setDecision(value);
            }

            keys.add(key);
        }

        // Check that an answer was supplied for this question.
        if(answer == null || answer.trim().length() == 0) {
            throw new PageValidationException("NoAnswerProvided", "No answer was supplied for this question.");
        }

        // Check that the answer supplied was a valid response.
        if(!keys.contains(answer)) {
            throw new PageValidationException("InvalidOption","The answer '"+answer+" was not a valid option.");
        }
    }
}
