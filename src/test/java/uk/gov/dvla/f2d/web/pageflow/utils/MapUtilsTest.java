package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;

import java.io.IOException;
import java.util.Map;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

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
        try {
            MedicalForm form = PageFlowCacheManager.getMedicalForm(NOTIFY_SERVICE);

            final String fullData = MapUtils.mapModelToString(form);

            assertNotNull(fullData);
            assertTrue("Questionnaire was not pre-populated", fullData.length() > 0);

            MedicalForm newForm = MapUtils.mapStringToModel(fullData);

            assertNotNull(newForm);

            Map<String, MedicalCondition> conditions = newForm.getSupportedConditions();

            assertTrue("No conditions were found in data structure", conditions.size() > 0);
            assertTrue("No questions were found in data structure", conditions.size() > 0);

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised.");

        } catch(IOException ex) {
            fail("An IOException should not have been raised.");
        }
    }
}
