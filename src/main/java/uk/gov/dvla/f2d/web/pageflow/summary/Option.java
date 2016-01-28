package uk.gov.dvla.f2d.web.pageflow.summary;

import java.util.Map;

public class Option
{
    private Map<String, Answer> options;

    Option() {
        super();
    }

    public Map<String, Answer> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Answer> options) {
        this.options = options;
    }
}
