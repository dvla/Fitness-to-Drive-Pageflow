package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.utils.ServiceUtils;

import java.io.IOException;
import java.io.InputStream;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.HYPHEN_SYMBOL;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.JSON_SUFFIX;

public class SummaryAggregator
{
    private static SummaryAggregator instance;

    private SummaryAggregator() {
        super();
    }

    public static synchronized SummaryAggregator getInstance() {
        if(instance == null) {
            instance = new SummaryAggregator();
        }
        return instance;
    }

    public Summary aggregate(MedicalForm form) {
        // Check to see if the quested service is supported?
        ServiceUtils.checkServiceSupported(form.getMessageHeader().getService());

        // Service is supported, so proceed to load the configuration.
        try {
            final String condition = form.getMedicalCondition().getID();
            final String service = form.getMessageHeader().getService();

            String resourceToLoad = condition + HYPHEN_SYMBOL + service + JSON_SUFFIX;

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            return new ObjectMapper().readValue(resourceStream, Summary.class);

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
