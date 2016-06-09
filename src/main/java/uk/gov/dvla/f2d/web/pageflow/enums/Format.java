package uk.gov.dvla.f2d.web.pageflow.enums;

public enum Format
{
    RADIO("Radio"),
    CHECKBOX("CheckBox"),
    FORM("Form"),
    CONTINUE("Continue");

    private String name;

    Format(final String name) {
        this.name = name;
    }

    public static Format lookup(final String name) {
        for(Format format : Format.values()) {
            if(format.getName().equals(name)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Format [" + name + "] is not currently supported!");
    }

    public String getName() {
        return this.name;
    }
}
