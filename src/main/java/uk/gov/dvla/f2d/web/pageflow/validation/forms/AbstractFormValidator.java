package uk.gov.dvla.f2d.web.pageflow.validation.forms;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;

public abstract class AbstractFormValidator
{
    private Map<String, String[]> values;
    private MedicalQuestion question;

    AbstractFormValidator() {
        super();
    }

    public void setFormData(Map<String, String[]> values) {
        this.values = values;
    }

    public void setQuestion(MedicalQuestion question) {
        this.question = question;
    }

    String getFormField(final String name) {
        if (values.keySet().contains(name)) {
            return (values.get(name).length > 0 ? values.get(name)[0] : EMPTY);
        }
        return EMPTY;
    }

    MedicalQuestion getQuestion() {
        return this.question;
    }

    void persistFields(final String[] fields) {
        List<String> answers = new ArrayList<String>();
        for (String field : fields) {
            answers.add(getFormField(field));
        }
        getQuestion().setAnswers(answers);
    }
}
