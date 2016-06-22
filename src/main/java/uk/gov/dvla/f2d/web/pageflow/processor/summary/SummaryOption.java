package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import java.util.Map;

public class SummaryOption
{
    private Map<String, SummaryAnswer> options;

    SummaryOption() {
        super();
    }

    public Map<String, SummaryAnswer> getOptions() {
        return options;
    }

    public void setOptions(Map<String, SummaryAnswer> options) {
        this.options = options;
    }
}
