package uk.gov.dvla.f2d.web.pageflow.exceptions;

import java.util.Map;

public class FlowStateModifiedException extends Exception
{
    private static final String START_PAGE      = "start";
    private static final String FINISH_PAGE     = "finish";

    private Map<String, String> options;

    public FlowStateModifiedException(Map<String, String> options) {
        this.options = options;
    }

    public String getStart() {
        return options.get(START_PAGE);
    }

    public String getFinish() {
        return options.get(FINISH_PAGE);
    }
}
