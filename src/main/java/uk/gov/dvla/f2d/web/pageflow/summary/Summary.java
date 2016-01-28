package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Summary
{
    private Map<String, Option> questions;

    Summary() {
        super();
    }

    public Map<String, Option> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Option> questions) {
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
