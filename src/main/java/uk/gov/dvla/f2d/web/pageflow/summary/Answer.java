package uk.gov.dvla.f2d.web.pageflow.summary;

import java.util.Map;

public class Answer
{
    private Map<String, String> answers;

    Answer() {
        super();
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
