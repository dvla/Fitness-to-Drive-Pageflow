package uk.gov.dvla.f2d.web.pageflow.model;

import java.util.List;

public class MedicalQuestion
{
    private Long ID;
    private String page;
    private String type;
    private Double index;
    private String format;
    private String validate;
    private String logout;
    private String options;
    private List<String> answers;

    private String decision;

    public MedicalQuestion() {
        super();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long iD) {
        ID = iD;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getIndex() {
        return index;
    }

    public void setIndex(Double index) {
        this.index = index;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String toString() {
        return "["+ID+"; "+page+"; "+type+"; "+index+"; "+format+"; "+validate+"; "+logout+"; "+ options +"; "+answers+"; "+decision+"]";
    }
}