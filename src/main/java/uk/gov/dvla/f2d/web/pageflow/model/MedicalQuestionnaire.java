package uk.gov.dvla.f2d.web.pageflow.model;

import java.io.Serializable;
import java.util.Map;

public class MedicalQuestionnaire implements Serializable
{
    private Map<String, MedicalCondition> conditions;

    public MedicalQuestionnaire() {
        super();
    }

    public Map<String, MedicalCondition> getConditions() {
        return this.conditions;
    }

    public void setConditions(Map<String, MedicalCondition> conditions) {
        this.conditions = conditions;
    }
}
