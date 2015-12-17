package uk.gov.dvla.f2d.web.pageflow.model;

import java.io.Serializable;
import java.util.List;

public class MedicalQuestionnaire implements Serializable
{
    private List<MedicalCondition> conditions;

    public MedicalQuestionnaire() {
        super();
    }

    public List<MedicalCondition> getConditions() {
        return this.conditions;
    }

    public void setConditions(List<MedicalCondition> conditions) {
        this.conditions = conditions;
    }
}
