package uk.gov.dvla.f2d.web.pageflow.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;
import uk.gov.dvla.f2d.web.pageflow.exceptions.FlowStateModifiedException;
import uk.gov.dvla.f2d.web.pageflow.exceptions.VerificationRequiredException;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.DataProcessorFactory;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;
import uk.gov.dvla.f2d.web.pageflow.validation.FormValidator;

import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;

public final class PageFlowManager
{
    private static final Logger logger = LoggerFactory.getLogger(PageFlowManager.class);

    private static final String FINISH_ATTRIBUTE        = "finish";

    private MedicalForm form;

    public PageFlowManager(MedicalForm form) {
        this.form = form;
    }

    public Map<String, MedicalCondition> getSupportedConditions() {
        logger.info("Fetch all supported medical conditions...");
        Service service = Service.lookup(form.getMessageHeader().getService());
        return PageFlowCacheManager.getInstance().getConditions(service);
    }

    public MedicalQuestion prepareQuestion(final String page)
        throws VerificationRequiredException, FlowStateModifiedException {

        logger.info("prepareQuestion("+page+")...");

        performIntegrityCheck();

        MedicalCondition condition = form.getMedicalCondition();
        MedicalQuestion question = condition.getQuestions().get(page);

        checkVerificationRequired(question);

        Format format = Format.lookup(question.getType());

        if(format.equals(Format.CONTROLLER)) {
            prepareCustomController(question);
        }

        return form.getMedicalCondition().getQuestions().get(page);
    }

    public PageResult process(PageForm pageForm, MedicalQuestion medicalQuestion) {
        performIntegrityCheck();
        performFormValidation(pageForm, medicalQuestion);

        PageResult result = new PageResult();
        result.setFlowFinished(medicalQuestion.getDecision().equals(ASTERISC));
        result.setErrorsFound(!(form.getMessageHeader().getNotifications().isEmpty()));
        result.setNextPage(null);
        return result;
    }

    public void updateBreadcrumb(MedicalQuestion question) {
        logger.debug("Update Breadcrumb: [" + question.getStep() + "]");

        List<String> breadcrumb = form.getMessageHeader().getBreadcrumb();

        if (breadcrumb.contains(question.getStep())) {
            int position = breadcrumb.indexOf(question.getStep());

            List<String> pre = breadcrumb.subList(0, (position + 1));
            List<String> post = breadcrumb.subList((position + 1), breadcrumb.size());

            for (String step : post) {
                MedicalQuestion myQuestion = getQuestion(step);
                myQuestion.getAnswers().clear();
                myQuestion.setDecision(EMPTY);
            }

            form.getMessageHeader().setBreadcrumb(pre);

        } else {
            form.getMessageHeader().getBreadcrumb().add(question.getStep());
        }
    }

    private void checkVerificationRequired(MedicalQuestion question) throws VerificationRequiredException {
        if(question.getPage().equals(Page.VERIFIED.getName())) {
            if(!(form.getMessageHeader().getAuthentication().isVerified())) {
                throw new VerificationRequiredException("Verification Required: ["+question.getStep()+"]");
            }
        }
    }

    private void performIntegrityCheck() {
        if(form.getMedicalCondition() == null) {
            throw new IllegalArgumentException("Medical condition has not been defined.");
        }
    }

    private void performFormValidation(PageForm pageForm, MedicalQuestion medicalQuestion) {
        new FormValidator(pageForm).validate(form, medicalQuestion);
    }

    private void prepareCustomController(MedicalQuestion question) throws FlowStateModifiedException {
        DataProcessorFactory factory = new DataProcessorFactory();
        IDataQuestionProcessor processor = factory.getQuestionProcessor(question);

        Map<String, String> configuration = processor.getConfiguration();
        question.setDecision(configuration.get(FINISH_ATTRIBUTE));

        throw new FlowStateModifiedException(configuration);
    }

    public MedicalQuestion getQuestion(final String step) {
        performIntegrityCheck();
        for (MedicalQuestion question : form.getMedicalCondition().getQuestions().values()) {
            if (question.getStep().equals(step)) {
                return question;
            }
        }
        throw new IllegalArgumentException("No question has been configured for this scenario!");
    }

    public List<SummaryLine> transform() {
        return DataTransformPipeline.create().transform(form);
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
