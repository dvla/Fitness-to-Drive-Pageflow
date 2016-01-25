package uk.gov.dvla.f2d.web.pageflow.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class PageFlowDataCacheTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageFlowDataCacheTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( PageFlowDataCacheTest.class );
    }

    /**
     * Test to see if a medical questionnaire has been loaded from resources
     */
    public void testMedicalFormLoadedFromLocalResource() {
        MedicalForm medical = PageFlowDataCache.getMedicalForm(NOTIFY_SERVICE);
        assertNotNull(medical);
    }
}
