package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Question
{
    ELIGIBILITY("Eligibility"),
    STANDARD("Standard");

    private String type;

    Question(final String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
