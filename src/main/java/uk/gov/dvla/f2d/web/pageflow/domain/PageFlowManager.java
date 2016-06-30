package uk.gov.dvla.f2d.web.pageflow.domain;

import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.cache.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;

import java.util.List;
import java.util.Map;

public class PageFlowManager
{
    private MedicalForm form;

    public PageFlowManager(MedicalForm form) {
        this.form = form;
    }

    public Map<String, MedicalCondition> getConditions() {
        Service service = Service.lookup(form.getMessageHeader().getService());
        return PageFlowCacheManager.getInstance().getConditions(service);
    }

    public MedicalQuestion getQuestion(final String step) {
        if(form.getMedicalCondition() == null) {
            throw new IllegalArgumentException("Medical condition has not been defined.");
        }

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
