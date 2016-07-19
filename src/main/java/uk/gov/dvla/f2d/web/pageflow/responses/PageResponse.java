package uk.gov.dvla.f2d.web.pageflow.responses;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

public class PageResponse
{
    private Boolean flowFinished;
    private Boolean errorsFound;
    private MedicalQuestion nextQuestion;

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

    public MedicalQuestion getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(MedicalQuestion nextQuestion) {
        this.nextQuestion = nextQuestion;
    }
}
