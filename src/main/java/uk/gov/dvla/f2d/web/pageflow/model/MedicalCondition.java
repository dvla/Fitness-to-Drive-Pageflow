package uk.gov.dvla.f2d.web.pageflow.model;

import java.util.Map;

public class MedicalCondition
{
    private Long ID;
    private String displayText;
    private String value;
    private String spellings;
    private String informationLink;
    private String configuration;
    private Map<String, MedicalQuestion> questions;

    public MedicalCondition() {
        super();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSpellings() {
        return spellings;
    }

    public void setSpellings(String spellings) {
        this.spellings = spellings;
    }

    public String getInformationLink() {
        return informationLink;
    }

    public void setInformationLink(String informationLink) {
        this.informationLink = informationLink;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Map<String, MedicalQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, MedicalQuestion> questions) {
        this.questions = questions;
    }

    public String toString() {
        return "["+ID+"; "+displayText+"; "+value+"; "+spellings+"; "+informationLink+"; "+configuration+"]";
    }
}