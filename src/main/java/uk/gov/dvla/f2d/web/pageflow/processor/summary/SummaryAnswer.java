package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import java.util.Map;

public class SummaryAnswer
{
    private Map<String, String> answers;

    SummaryAnswer() {
        super();
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
