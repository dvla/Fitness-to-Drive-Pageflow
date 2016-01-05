package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Format
{
    FORM("Form"),
    RADIO("Radio"),
    CHECKBOX("Checkbox"),
    CONTINUE("Continue");

    private String format;

    private Format(final String newFormat) {
        this.format = newFormat;
    }

    public boolean equals(final String newFormat) {
        return this.format.equalsIgnoreCase(newFormat);
    }

    public String toString() {
        return this.format;
    }
}
