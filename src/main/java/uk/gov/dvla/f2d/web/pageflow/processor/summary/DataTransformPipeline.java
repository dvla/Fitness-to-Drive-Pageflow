package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import uk.gov.dvla.f2d.model.pageflow.MedicalForm;

import java.util.List;

public final class DataTransformPipeline
{
    private static DataTransformPipeline instance;

    // *****************************************************************************************
    // No internal/member variables defined as we do not want to share this data just yet. In
    // future, we will optimise the pipeline to cache reusable data and prevent multi-fetch and
    // transpose of configurations. We do not have the data footprint/structure for this yet.
    // *****************************************************************************************

    private DataTransformPipeline() {
        super();
    }

    public static synchronized DataTransformPipeline create() {
        if(instance == null) {
            instance = new DataTransformPipeline();
        }
        return instance;
    }

    public List<SummaryLine> transform(MedicalForm form) {
        return new DataTransformDelegate(form).process();
    }
}
