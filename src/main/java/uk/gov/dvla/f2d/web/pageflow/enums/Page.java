package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Page
{
    UNVERIFIED("Unverified"),
    VERIFIED("Verified");

    private String name;

    Page(final String page) {
        this.name = page;
    }

    public static Page lookup(final String name) {
        for(Page page : Page.values()) {
            if(page.getName().equals(name)) {
                return page;
            }
        }
        throw new IllegalArgumentException("Page [" + name + "] is not currently supported!");
    }

    public String getName() {
        return this.name;
    }
}
