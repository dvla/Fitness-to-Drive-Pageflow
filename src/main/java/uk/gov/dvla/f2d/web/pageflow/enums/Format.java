package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Format
{
    RADIO("Radio"),
    CHECKBOX("CheckBox"),
    FORM("Form"),
    CONTINUE("Continue");

    private String format;

    Format(final String newFormat) {
        this.format = newFormat;
    }

    public boolean equals(final String newFormat) {
        return format.equals(newFormat);
    }

    public String toString() {
        return this.format;
    }
}
