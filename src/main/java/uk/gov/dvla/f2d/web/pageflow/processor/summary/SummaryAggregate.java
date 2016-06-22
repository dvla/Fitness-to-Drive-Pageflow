package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SummaryAggregate
{
    private Map<String, SummaryOption> questions;

    SummaryAggregate() {
        super();
    }

    public Map<String, SummaryOption> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, SummaryOption> questions) {
        this.questions = questions;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
