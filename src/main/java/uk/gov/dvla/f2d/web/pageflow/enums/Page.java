package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Page
{
    ELIGIBILITY("Eligibility"),
    INFORMATION("Information"),
    QUESTION("Question"),
    SUMMARY("Summary");

    private String type;

    Page(final String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }
}
