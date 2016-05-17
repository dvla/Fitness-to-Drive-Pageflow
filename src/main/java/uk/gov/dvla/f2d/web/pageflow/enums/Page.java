package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Page
{
    UNVERIFIED("Unverified"),
    VERIFIED("Verified");

    private String page;

    Page(final String page) {
        this.page = page;
    }

    public String toString() {
        return this.page;
    }
}
