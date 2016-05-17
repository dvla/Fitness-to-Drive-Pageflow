package uk.gov.dvla.f2d.web.pageflow.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

import java.util.Map;

public class MapUtilsTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public MapUtilsTest(String testName ) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MapUtilsTest.class);
    }

    /**
     * Test to see if a medical questionnaire has been loaded from resources
     */
    public void testRetrieveSupportedMedicalConditionsForNotifyService() {
        String service = Service.NOTIFY.toString();
        Map<String, MedicalCondition> conditions = PageFlowCacheManager.getSupportedConditions(service);

        assertTrue("No conditions were found in data structure", conditions.size() > 0);
        assertTrue("No questions were found in data structure", conditions.size() > 0);
    }
}
