package uk.gov.dvla.f2d.web.pageflow.domain;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.DataTransformPipeline;
import uk.gov.dvla.f2d.web.pageflow.processor.summary.SummaryLine;

import java.util.List;

public class PageFlowManager
{
    private MedicalForm form;

    public PageFlowManager(MedicalForm form) {
        this.form = form;
    }

    public List<SummaryLine> transform() {
        return DataTransformPipeline.create().transform(form);
    }
}
