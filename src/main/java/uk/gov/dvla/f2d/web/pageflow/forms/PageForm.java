package uk.gov.dvla.f2d.web.pageflow.forms;

import java.util.Map;
import java.util.TreeMap;

public class PageForm
{
    private String question;
    private Map<String, String[]> entities;

    public PageForm() {
        setEntities(new TreeMap<>());
    }

    public PageForm(Map<String, String[]> entities) {
        setEntities(entities);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String[]> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, String[]> entities) {
        this.entities = entities;
    }
}
