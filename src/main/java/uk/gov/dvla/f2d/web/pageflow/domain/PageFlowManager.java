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
import uk.gov.dvla.f2d.web.pageflow.exceptions.PageNotFoundException;
import uk.gov.dvla.f2d.web.pageflow.exceptions.VerificationRequiredException;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.DataProcessorFactory;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.FormValidator;
import uk.gov.dvla.f2d.web.pageflow.responses.PageResponse;

import java.util.List;
import java.util.Map;

import static uk.gov.dvla.f2d.model.constants.StringConstants.ASTERISC;
import static uk.gov.dvla.f2d.model.constants.StringConstants.EMPTY;
import static uk.gov.dvla.f2d.model.constants.StringConstants.FORWARD_SLASH;

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

        updateBreadcrumb(question);

        return question;
    }

    public PageResponse processQuestion(PageForm pageForm) {
        performIntegrityCheck();

        MedicalCondition condition = form.getMedicalCondition();

        Map<String, MedicalQuestion> questions = condition.getQuestions();
        MedicalQuestion current = questions.get(pageForm.getQuestion());

        performFormValidation(pageForm, current);

        PageResponse response = new PageResponse();
        response.setFlowFinished(current.getDecision().equals(ASTERISC));
        response.setErrorsFound(!(form.getMessageHeader().getNotifications().isEmpty()));

        if(!response.isErrorsFound()) {
            response.setNextQuestion(current);
        } else {
            if(!response.isFlowFinished()) {
                for(MedicalQuestion next : condition.getQuestions().values()) {
                    if(next.getStep().equalsIgnoreCase(current.getDecision())) {
                        response.setNextQuestion(next);
                    }
                }
            } else {
                response.setNextQuestion(null);
            }
        }

        return response;
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

    private MedicalQuestion findQuestionInBreadcrumb(final int depth) throws PageNotFoundException {
        List<String> path = form.getMessageHeader().getBreadcrumb();
        if(path.isEmpty()) {
            throw new PageNotFoundException("Breadcrumb is empty");
        }
        if(depth > path.size()) {
            throw new PageNotFoundException("Breadcrumb is "+path.size()+", depth is "+depth);
        }
        return getQuestion(path.get(path.size() - depth));
    }

    public MedicalQuestion findPreviousQuestion() throws PageNotFoundException {
        return findQuestionInBreadcrumb(-1);
    }

    public MedicalQuestion findCurrentQuestion() throws PageNotFoundException {
        return findQuestionInBreadcrumb(0);
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
        new FormValidator(form, medicalQuestion).validate(pageForm);
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
}
