package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Format
{
    RADIO("Radio"),
    CHECKBOX("Checkbox"),
    CONFIRM("Confirm");

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
