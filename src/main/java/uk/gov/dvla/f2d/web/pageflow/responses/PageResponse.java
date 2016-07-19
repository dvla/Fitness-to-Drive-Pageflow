package uk.gov.dvla.f2d.web.pageflow.responses;

public class PageResponse
{
    private Boolean flowFinished;
    private Boolean errorsFound;

    public PageResponse() {
        super();
    }

    public Boolean isFlowFinished() {
        return flowFinished;
    }

    public void setFlowFinished(Boolean flowFinished) {
        this.flowFinished = flowFinished;
    }

    public Boolean isErrorsFound() {
        return errorsFound;
    }

    public void setErrorsFound(Boolean errorsFound) {
        this.errorsFound = errorsFound;
    }
}
