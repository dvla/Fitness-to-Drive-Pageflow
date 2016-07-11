package uk.gov.dvla.f2d.web.pageflow.forms;

import java.util.Map;
import java.util.TreeMap;

public class PageForm
{
    private Map<String, String[]> entities;

    public PageForm() {
        setEntities(new TreeMap<>());
    }

    public PageForm(Map<String, String[]> entities) {
        setEntities(entities);
    }

    public Map<String, String[]> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, String[]> entities) {
        this.entities = entities;
    }
}
