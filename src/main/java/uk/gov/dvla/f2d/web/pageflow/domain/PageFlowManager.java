package uk.gov.dvla.f2d.web.pageflow.domain;

import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;
import uk.gov.dvla.f2d.web.pageflow.validation.FormValidator;

import java.util.List;
import java.util.Map;
import static uk.gov.dvla.f2d.model.constants.StringConstants.ASTERISC;

public final class PageFlowManager
{
    private MedicalForm medicalForm;

    public PageFlowManager(MedicalForm form) {
        this.medicalForm = form;
    }

    public Map<String, MedicalCondition> getConditions() {
        Service service = Service.lookup(medicalForm.getMessageHeader().getService());
        return PageFlowCacheManager.getInstance().getConditions(service);
    }

    public PageResult process(PageForm pageForm, MedicalQuestion medicalQuestion) {
        performIntegrityCheck();
        performFormValidation(pageForm, medicalQuestion);

        PageResult result = new PageResult();
        result.setFlowFinished(medicalQuestion.getDecision().equals(ASTERISC));
        result.setErrorsFound(!(medicalForm.getMessageHeader().getNotifications().isEmpty()));
        result.setNextPage(null);
        return result;
    }

    private void performIntegrityCheck() {
        if(medicalForm.getMedicalCondition() == null) {
            throw new IllegalArgumentException("Medical condition has not been defined.");
        }
    }

    private void performFormValidation(PageForm pageForm, MedicalQuestion medicalQuestion) {
        new FormValidator(pageForm).validate(medicalForm, medicalQuestion);
    }

    public MedicalQuestion getQuestion(final String step) {
        performIntegrityCheck();
        for (MedicalQuestion question : medicalForm.getMedicalCondition().getQuestions().values()) {
            if (question.getStep().equals(step)) {
                return question;
            }
        }
        throw new IllegalArgumentException("No question has been configured for this scenario!");
    }

    public List<SummaryLine> transform() {
        return DataTransformPipeline.create().transform(medicalForm);
    }

    public class PageResult
    {
        private Boolean flowFinished;
        private Boolean errorsFound;
        private String nextPage;

        PageResult() {
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


        public String getNextPage() {
            return nextPage;
        }

        public void setNextPage(String nextPage) {
            this.nextPage = nextPage;
        }
    }
}
